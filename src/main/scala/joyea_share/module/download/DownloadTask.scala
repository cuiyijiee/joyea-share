package joyea_share.module.download

import java.io.{File, FileFilter}
import java.util
import java.util.{Date, UUID}

import com.json.{JsonArray, JsonObject}
import com.utils.{CommonUtil, FtpUtil}
import joyea_share.service.RedisService
import joyea_share.util.{CommonListener, LenovoUtil, SUtil, ZipUtils}
import xitrum.{Config, Log}
import scala.concurrent.ExecutionContext.Implicits.global

case class DownloadTask(
                         id: String = UUID.randomUUID().toString,
                         downloadFile: java.util.List[DownloadItem] = new util.ArrayList[DownloadItem](),
                         downloadRoleName: String,
                         downloadRoleId: String
                       ) extends Log {

  def toJson(): JsonObject = {

    val fileArr = new JsonArray()
    downloadFile.forEach(item => {
      fileArr.add(item.toJson())
    })

    new JsonObject()
      .add("id", id)
      .add("file", fileArr)
      .add("downloadRoleName", downloadRoleName)
      .add("downloadRoleId", downloadRoleId)
      .add("startTime", SUtil.genDateString(startDate))
      .add("firstSrcName", downloadFile.get(0).fileName)
      .add("finishTime", if (finishDate == null) "" else SUtil.genDateString(finishDate))

  }

  private var status: DownloadStatus.Value = DownloadStatus.READY
  private val startDate: java.util.Date = new Date()
  private var finishDate: java.util.Date = _
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

    downloadListener.onStart(id, downloadFile.size())
    status = DownloadStatus.DOWNLOAD
    downloadFile.forEach(item => {
      RedisService.recordFileDownload(item.neid).foreach(resp => {
        log.error(s"record download ${item.neid} with result : $resp")
      })
      LenovoUtil.downloadFileV2(sessionId, item.path, item.neid, item.rev, "ent",
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
    downloadListener.onNext(id, downloadProgress, downloadFile.size())
    if (downloadProgress >= downloadFile.size()) {
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
        ftpUtil.downloadFile("/download/" + id + "/" + file.getName, file.getAbsolutePath, true, maxWaitSeconds)
      })
      ftpUtil.logout()

      ZipUtils.compressZip(Array(saveFilePath), saveCompressPath + ".zip")
      //CommonUtil.delete(saveFilePath)
      finishDate = new Date()
      status = DownloadStatus.FINISH
      downloadListener.onFinish(taskId = id, successNum, failNum, downloadFile.size())
    }
  }

  def clear(): Unit = {

  }

  def queryStatus(): DownloadStatus.Value = {
    this.status
  }

}
