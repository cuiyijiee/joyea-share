package joyea_share.module.download

import java.io.{File, FileFilter}
import java.time.LocalDateTime
import java.util.{Date, UUID}

import com.utils.{CommonUtil, FtpUtil}
import joyea_share.service.RedisService
import joyea_share.util.{BaseJsonFormat, CommonListener, LenovoUtil, ZipUtils}
import org.json4s.jackson.Serialization
import xitrum.{Config, Log}

import scala.concurrent.ExecutionContext.Implicits.global

case class DownloadTask(
                         id: String = UUID.randomUUID().toString,
                         downloadFile: Seq[DownloadRecord],
                         downloadRoleName: String,
                         downloadRoleId: String,
                         startTime: LocalDateTime = LocalDateTime.now,
                         finishTime: Option[LocalDateTime] = None,
                         firstSrcName: Option[String] = None,
                       ) extends Log with BaseJsonFormat {

  def toJson(): String = {
    Serialization.write(this.copy(finishTime = Option(finishDate), firstSrcName = downloadFile.headOption.map(_.fileName)))
  }

  private var status: DownloadStatus.Value = DownloadStatus.READY
  private var finishDate: LocalDateTime = _
  private var saveFilePath: String = ""
  private var saveCompressPath: String = ""
  private var downloadListener: DownloadListener = _
  private var downloadProgress: Int = 0
  private var successNum: Int = 0
  private var failNum: Int = 0
  //超时时间为2分钟，超过时间则下载失败
  //private val DOWNLOAD_TIMEOUT = 2 * 60 * 1000

  def execute(sessionId: String, baseSavePath: String, baseCompressSavePath: String, listener: DownloadListener): Unit = {
    downloadListener = listener
    saveFilePath = baseSavePath + "/" + id
    saveCompressPath = baseCompressSavePath + "/" + id

    new File(saveFilePath).mkdirs()

    downloadListener.onStart(id, downloadFile.length)
    status = DownloadStatus.DOWNLOAD
    downloadFile.foreach(item => {
      RedisService.recordFileDownload(item.neid.toString).foreach(resp => {
        log.error(s"record download ${item.neid} with result : $resp")
      })
      LenovoUtil.downloadFileV2(sessionId, item.filePath, item.neid, item.rev, "ent",
        saveFilePath + "/" + item.index + "--" + item.fileName, new CommonListener[File] {
          override def onSuccess(obj: File): Unit = {
            successNum = successNum + 1
            downloadProgress = downloadProgress + 1
            log.info(s"下载文件【$item】成功:${obj.getAbsolutePath}")
            checkFinish()
          }

          override def onError(error: String): Unit = {
            try {
              failNum = failNum + 1
              downloadProgress = downloadProgress + 1
              log.error(s"下载文件【$item】失败:$error")
              checkFinish()
            } catch {
              case e: Exception =>
                log.error(s"download file exist error:${error}")
            }
          }
        })
    })
  }

  def checkFinish(): Unit = this.synchronized {
    //下载完成
    downloadListener.onNext(id, downloadProgress, downloadFile.length)
    if (downloadProgress >= downloadFile.length) {
      //线程等待2秒，避免加密软件冲突
      CommonUtil.writeFile(s"$saveFilePath/请勿外泄.txt", "仅一公司内部资料，请勿外泄！")

      //先上传文件到ftp服务器进行加密再下载下来
      val ftpUtil = new FtpUtil(
        Config.application.getConfig("download").getString("ftp_ip"),
        Config.application.getConfig("download").getInt("ftp_port"),
        Config.application.getConfig("download").getString("ftp_name"),
        Config.application.getConfig("download").getString("ftp_pass")
      )
      ftpUtil.login()
      val fileList = new File(saveFilePath).listFiles(new FileFilter {
        override def accept(file: File): Boolean = file.isFile
      })
      fileList.foreach(file => {
        ftpUtil.uploadFileToRemoteDir(file.getAbsolutePath, "/download/" + id)
        file.delete()
      })
      val maxWaitSeconds = Config.application.getConfig("download").getInt("max_wait_seconds") * 1000
      fileList.foreach(file => {
        ftpUtil.downloadFile("/download/" + id + "/" + file.getName, file.getAbsolutePath,
          true, true, maxWaitSeconds)
      })
      ftpUtil.deleteDir("/download/" + id)
      ftpUtil.logout()

      ZipUtils.compressZip(Array(saveFilePath), saveCompressPath + ".zip")
      //CommonUtil.delete(saveFilePath)
      finishDate = LocalDateTime.now()
      status = DownloadStatus.FINISH
      downloadListener.onFinish(taskId = id, successNum, failNum, downloadFile.length)
    }
  }

  def clear(): Unit = {

  }

  def queryStatus(): DownloadStatus.Value = {
    this.status
  }

}
