package joyea_share.module.download

import java.io.{File, FileFilter}
import java.time.LocalDate
import java.time.chrono.ChronoLocalDate
import java.util.concurrent.{ExecutorService, Executors, TimeUnit}
import com.utils.CommonUtil
import joyea_share.model.{SrcQuote, UploadRecord}
import joyea_share.util.{BaseJsonFormat, CommonListener, LenovoUtil, SUtil, VideoTranscodeUtil}
import xitrum.{Config, Log}

import scala.concurrent.ExecutionContext

trait DownloadManager {
  DownloadManager.init()
}


object DownloadManager extends Log with BaseJsonFormat {

  private var adminSessionId: String = ""
  private val getAdminTokenExecutor = Executors.newScheduledThreadPool(1)
  private val executor: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors())

  //用来存放所有的任务，每天清除一次
  //private val downloadTaskMap: java.util.Map[String, DownloadTask] = new java.util.HashMap[String, DownloadTask]()

  private var newDownloadTaskMap: Map[String, DownloadTask] = Map()

  private var currentIndex: Long = 0L

  def init(): Unit = {

    getAdminTokenExecutor.scheduleWithFixedDelay(() => {

      try {
        genNewSession()
        getTodayDownloadDir(true)
        getTodayDownloadDir(false)
        filterNotTodayTask()

      } catch {
        case e: Exception =>
          log.error("gen new admin session error: ", e)
      }

    }, 0, 30, TimeUnit.MINUTES)


  }


  //传入ID,返回一个任务的ID，并开始下载
  def doDownload(task: DownloadTask): Unit = {
    newDownloadTaskMap = newDownloadTaskMap ++ Map(task.id -> task)
    executor.execute(() => {
      task.execute(sessionId = adminSessionId,
        baseSavePath = getTodayDownloadDir(false).getAbsolutePath,
        baseCompressSavePath = getTodayDownloadDir(true).getAbsolutePath,
        listener = new DownloadListener {
          override def onStart(taskId: String, total: Int): Unit = {

          }

          override def onNext(taskId: String, progress: Int, total: Int): Unit = {

          }

          override def onFinish(taskId: String, success: Int, failed: Int, total: Int): Unit = {
            implicit val ctx: ExecutionContext = ExecutionContext.Implicits.global
            task.downloadFile.foreach(record => {
              record.recordToDb()
              UploadRecord.findByNeid(record.neid)
                .foreach(recordOpt => {
                  if (recordOpt.isDefined) {
                    SrcQuote.create(record.neid, recordOpt.get.uploader, record.joyeaUserId)
                  } else {
                    log.info(s"record ${record.neid} is not upload by server, no need to record src quote!")
                  }
                })
            })
          }
        })
    })
  }

  //过滤掉不属于今天的任务
  def filterNotTodayTask(): Unit = {
    newDownloadTaskMap = newDownloadTaskMap.filter(entry => {
      entry._2.startTime.toLocalDate.isAfter(ChronoLocalDate.from(LocalDate.now().atStartOfDay()))
    })
  }

  def getTodayDownloadDir(isCompressed: Boolean): File = {
    val filePath = if (isCompressed) {
      "download/compressed/" + SUtil.genDateString(formatString = "yy-MM-dd")
    } else {
      "download/origin/" + SUtil.genDateString(formatString = "yy-MM-dd")
    }
    val saveDir = new File(filePath)
    if (!saveDir.exists()) {
      saveDir.mkdirs()
    }
    saveDir.getParentFile.listFiles(new FileFilter {
      override def accept(pathname: File): Boolean = !pathname.getName.equals(saveDir.getName)
    }).foreach(file => {
      CommonUtil.delete(file)
    })
    saveDir
  }

  def queryTask(taskId: String): Option[DownloadTask] = {
    newDownloadTaskMap.get(taskId)
  }

  def genNewSession(): Unit = {
    val adminConfig = Config.application.getConfig("admin")
    LenovoUtil.login(adminConfig.getString("name"), adminConfig.getString("pwd"), new CommonListener[(String, String, Long)] {
      override def onSuccess(obj: (String, String, Long)): Unit = {
        log.info(s"获取到最高权限SessionId：$obj")
        adminSessionId = obj._1

        if (currentIndex % 2 == 0) {
          log.info("开始执行视频转码检查")
          VideoTranscodeUtil.startJob()
        } else {
          log.info("下次执行视频转码检查")
        }
        currentIndex += 1
      }

      override def onError(error: String): Unit = {
        log.error(s"获取到最高权限SessionId失败：$error")
      }
    })
  }

  def getTodayTaskMap(uid: String): Map[String, DownloadTask] = {
    newDownloadTaskMap.filter(_._2.downloadRoleId.equals(uid))
  }

  def getAdminToken: String = {
    adminSessionId
  }
}

object DownloadStatus extends Enumeration {
  type DownloadStatus = Value
  val NONE, READY, DOWNLOAD, FINISH = Value
}

trait DownloadListener {

  //开始下载
  def onStart(taskId: String, total: Int): Unit

  def onNext(taskId: String, progress: Int, total: Int): Unit

  def onFinish(taskId: String, success: Int, failed: Int, total: Int): Unit
}
