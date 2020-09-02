package joyea_share.module.download

import java.io.{File, FileFilter}
import java.util
import java.util.{Calendar, Date, Timer, TimerTask}
import java.util.concurrent.{ExecutorService, Executors, TimeUnit}

import com.json.{JsonArray, WriterConfig}
import com.utils.CommonUtil
import joyea_share.util.{CommonListener, Constant, LenovoUtil, SUtil}
import xitrum.{Config, Log}


trait DownloadManager {

  DownloadManager.init()

}


object DownloadManager extends Log {

  private val baseRecordJsonDir: File = new File(s"./log/download/")
  private var adminSessionId: String = ""
  private var baseSaveFilePath: String = ""
  private var baseCompressSaveFilePath: String = ""
  private val getAdminTokenExecutor = Executors.newScheduledThreadPool(4)
  private val executor: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors())

  //用来存放所有的任务，每天清除一次
  private val downloadTaskMap: java.util.Map[String, DownloadTask] = new util.HashMap[String, DownloadTask]()

  private val PERIOD_DAY = 24 * 60 * 60 * 1000

  def addDay(date: Date, num: Int): Date = {
    val startDT = Calendar.getInstance()
    startDT.setTime(date)
    startDT.add(Calendar.DAY_OF_MONTH, num)
    startDT.getTime
  }

  def init(): Unit = {
    initBaseSaveFilePath()
    baseRecordJsonDir.mkdirs()

    getAdminTokenExecutor.scheduleWithFixedDelay(() => {
      genNewSession()
    }, 0, 30, TimeUnit.MINUTES)

    val deleteDownloadFileTimer = new Timer()
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 2) //凌晨1点
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    var date = calendar.getTime() //第一次执行定时任务的时间
    //如果第一次执行定时任务的时间 小于当前的时间
    //此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
    if (date.before(new Date())) {
      date = this.addDay(date, 1);
    }
    deleteDownloadFileTimer.schedule(new TimerTask {
      override def run(): Unit = {
        val yesterdayDownloadDir = new File(s"${baseRecordJsonDir.getAbsolutePath}/${SUtil.genYesterdayDateString(formatString = "yy-MM-dd")}")
        CommonUtil.delete(yesterdayDownloadDir)
      }
    }, date, PERIOD_DAY)

    //整点刷新配置
    getAdminTokenExecutor.scheduleWithFixedDelay(() => {
      initBaseSaveFilePath()
      val saveDir = new File(baseSaveFilePath)
      saveDir.getParentFile.listFiles(new FileFilter {
        override def accept(pathname: File): Boolean = !pathname.getName.equals(saveDir.getName)
      }).foreach(file => {
        CommonUtil.delete(file)
      })
      val compressSaveDir = new File(baseCompressSaveFilePath)
      compressSaveDir.getParentFile.listFiles(new FileFilter {
        override def accept(pathname: File): Boolean = !pathname.getName.equals(compressSaveDir.getName)
      }).foreach(file => {
        CommonUtil.delete(file)
      })
    }, System.currentTimeMillis() - SUtil.getNextMinuteHour(0).getTime, 1 * 60 * 60 * 1000, TimeUnit.MILLISECONDS)
  }


  //传入ID,返回一个任务的ID，并开始下载
  def doDownload(task: DownloadTask): Unit = {
    downloadTaskMap.put(task.id, task)
    executor.execute(() => {
      task.execute(sessionId = adminSessionId, baseSavePath = baseSaveFilePath, baseCompressSavePath = baseCompressSaveFilePath, listener = new DownloadListener {
        override def onStart(taskId: String, total: Int): Unit = {

        }

        override def onNext(taskId: String, progress: Int, total: Int): Unit = {

        }

        override def onFinish(taskId: String, success: Int, failed: Int, total: Int): Unit = {
          val recordJson = getTodayRecordJson()
          val recordJsonArr = recordJson._1
          recordJsonArr.add(task.toJson()) //记录当前记录
          Config.xitrum.cache.put(Constant.DOWNLOAD_RECORD_CACHE_KEY, recordJsonArr) //回写缓存
          CommonUtil.writeFile(recordJson._2, recordJsonArr.toString(WriterConfig.PRETTY_PRINT)) //回写文件
        }
      })
    })
  }

  def getTodayRecordJson(): (JsonArray, File) = {
    val recordJsonFile = new File(s"${baseRecordJsonDir.getAbsolutePath}/${SUtil.genDateString(formatString = "yy-MM-dd")}")
    var recordJsonArr: JsonArray = null
    if (recordJsonFile.exists()) { //如果今天的日志文件存在，则首先从缓存中读，没有再从文件中读取
      recordJsonArr = Config.xitrum.cache.get(Constant.DOWNLOAD_RECORD_CACHE_KEY).getOrElse(
        JsonArray.readFrom(CommonUtil.readFile(recordJsonFile, "[]"))
      ).asInstanceOf[JsonArray]
    } else { //如果今天日志文件不存在，则创建新的记录
      recordJsonArr = new JsonArray()
    }
    (recordJsonArr, recordJsonFile)
  }


  def queryTask(taskId: String): DownloadStatus.Value = {
    val task = downloadTaskMap.get(taskId)
    if (task == null) DownloadStatus.NONE else task.queryStatus()
  }

  def initBaseSaveFilePath(): Unit = {
    val newPath = "download/" + SUtil.genDateString(formatString = "yy-MM-dd")
    val newCompressPath = "download/compressed/" + SUtil.genDateString(formatString = "yy-MM-dd")
    if (baseSaveFilePath.length != 0 && !baseSaveFilePath.equals(newPath)) {
      downloadTaskMap.clear()
    }
    baseCompressSaveFilePath = newCompressPath
    baseSaveFilePath = newPath
  }

  def getBaseCompressSaveFilePath(): String = {
    baseCompressSaveFilePath
  }

  def getBaseSaveFilePath(): String = {
    baseSaveFilePath
  }

  def getTodayTaskMap(): java.util.Map[String, DownloadTask] = {
    downloadTaskMap
  }

  //TODO 先检查session是否失效再请求新的session
  def genNewSession(): Unit = {
    val adminConfig = Config.application.getConfig("admin")
    LenovoUtil.login(adminConfig.getString("name"), adminConfig.getString("pwd"), new CommonListener[(String, String, Long)] {
      override def onSuccess(obj: (String, String, Long)): Unit = {
        log.info(s"获取到最高权限SessionId：$obj")
        adminSessionId = obj._1
      }

      override def onError(error: String): Unit = {
        log.error(s"获取到最高权限SessionId失败：$error")
      }
    })
  }

  def getAdminToken(): String = {
    adminSessionId
  }
}
