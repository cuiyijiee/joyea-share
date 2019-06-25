package joyea_share.module.download

import java.io.{File, FileFilter}
import java.util
import java.util.concurrent.{ExecutorService, Executors}
import java.util.{Calendar, Timer, TimerTask}

import com.json.{JsonArray, WriterConfig}
import com.utils.CommonUtil
import joyea_share.util.{CommonListener, LenovoUtil, SUtil}
import org.slf4s.{Logger, LoggerFactory}
import xitrum.Config


trait DownloadManager {

    DownloadManager.init()

}


object DownloadManager {

    private val recordJsonArr: JsonArray = new JsonArray()

    private val _log: Logger = LoggerFactory.getLogger(DownloadManager.getClass)
    private var adminSessionId: String = ""
    private var baseSaveFilePath: String = "./tmp/download/" + SUtil.genDateString(formatString = "yy-MM-dd")
    private val executor: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors())

    //用来存放所有的任务，每天清除一次
    private val downloadTaskMap: java.util.Map[String, DownloadTask] = new util.HashMap[String, DownloadTask]()

    def init(): Unit = {
        initBaseSaveFilePath()
        genNewSession()

        //整点刷新配置

        new Timer().schedule(new TimerTask {
            override def run(): Unit = {
                initBaseSaveFilePath()
                genNewSession()

                val saveDir = new File(baseSaveFilePath)
                saveDir.getParentFile.listFiles(new FileFilter {
                    override def accept(pathname: File): Boolean = !pathname.getName.equals(saveDir.getName)
                }).foreach(file => {
                    CommonUtil.delete(file)
                })
            }
        }, {
            //将第一次时间设置在当天晚上的11.59，省去了考虑设置的时间在当前时间之前的情况
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.getTime
        }, 1 * 60 * 60 * 1000L)
    }


    //传入ID,返回一个任务的ID，并开始下载
    def doDownload(task: DownloadTask): Unit = {
        downloadTaskMap.put(task.id, task)
        executor.execute(() => {
            task.execute(sessionId = adminSessionId, baseSavePath = baseSaveFilePath, listener = new DownloadListener {
                override def onStart(taskId: String, total: Int): Unit = {

                }

                override def onNext(taskId: String, progress: Int, total: Int): Unit = {

                }

                override def onFinish(taskId: String, success: Int, failed: Int, total: Int): Unit = {
                    recordJsonArr.add(task.toJson())
                    val baseFile = new File("./log/download")
                    baseFile.mkdirs()
                    CommonUtil.writeFile(s"${baseFile.getAbsolutePath}/${
                        SUtil.genDateString(formatString = "yy-MM-dd")
                    }", recordJsonArr.toString(WriterConfig.PRETTY_PRINT))
                }
            })
        })
    }


    def queryTask(taskId: String): DownloadStatus.Value = {
        val task = downloadTaskMap.get(taskId)
        if (task == null) DownloadStatus.NONE else task.queryStatus()
    }

    def initBaseSaveFilePath(): Unit = {
        baseSaveFilePath = Config.application.getConfig("download").getString("tmp_dir") +
            "/download/" + SUtil.genDateString(formatString = "yy-MM-dd")
    }

    def getBaseSaveFilePath(): String = {
        baseSaveFilePath
    }

    //TODO 先检查session是否失效再请求新的session
    def genNewSession(): Unit = {
        val adminConfig = Config.application.getConfig("admin")
        LenovoUtil.login(adminConfig.getString("name"), adminConfig.getString("pwd"), new CommonListener[(String, String, Long)] {
            override def onSuccess(obj: (String, String, Long)): Unit = {
                _log.info(s"获取到最高权限SessionId：$obj")
                adminSessionId = obj._1
            }

            override def onError(error: String): Unit = {
                _log.error(s"获取到最高权限SessionId失败：$error")
            }
        })
    }
}
