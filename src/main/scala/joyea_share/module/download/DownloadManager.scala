package joyea_share.module.download

import java.io.{File, FileFilter}
import java.util.concurrent.{ExecutorService, Executors, TimeUnit}

import com.utils.CommonUtil
import joyea_share.util.{CommonListener, LenovoUtil, SUtil}
import xitrum.{Config, Log}

trait DownloadManager {
    DownloadManager.init()
}


object DownloadManager extends Log {

    private var adminSessionId: String = ""
    private val getAdminTokenExecutor = Executors.newScheduledThreadPool(4)
    private val executor: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors())

    //用来存放所有的任务，每天清除一次
    private val downloadTaskMap: java.util.Map[String, DownloadTask] = new java.util.HashMap[String, DownloadTask]()

    def init(): Unit = {

        getAdminTokenExecutor.scheduleWithFixedDelay(() => {

            genNewSession()

            //删除非当天下载文件夹的文件，防止文件把服务器空间占满
            val saveDir = getTodayDownloadDir(true)
            saveDir.getParentFile.listFiles(new FileFilter {
                override def accept(pathname: File): Boolean = !pathname.getName.equals(saveDir.getName)
            }).foreach(file => {
                CommonUtil.delete(file)
            })
            val compressSaveDir = getTodayDownloadDir(false)
            compressSaveDir.getParentFile.listFiles(new FileFilter {
                override def accept(pathname: File): Boolean = !pathname.getName.equals(compressSaveDir.getName)
            }).foreach(file => {
                CommonUtil.delete(file)
            })

        }, 0, 30, TimeUnit.MINUTES)
    }


    //传入ID,返回一个任务的ID，并开始下载
    def doDownload(task: DownloadTask): Unit = {
        downloadTaskMap.put(task.id, task)
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
                        task.downloadFile.foreach(record => {
                            record.recordToDb()
                        })
                    }
                })
        })
    }

    def getTodayDownloadDir(isCompressed: Boolean): File = {
        val filePath = if (isCompressed) {
            "download/compressed/" + SUtil.genDateString(formatString = "yy-MM-dd")
        } else {
            "download/" + SUtil.genDateString(formatString = "yy-MM-dd")
        }
        val file = new File(filePath)
        if (file.exists()) {
            file.mkdirs()
        }
        file
    }

    def queryTask(taskId: String): DownloadStatus.Value = {
        val task = downloadTaskMap.get(taskId)
        if (task == null) DownloadStatus.NONE else task.queryStatus()
    }

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

    def getTodayTaskMap: java.util.Map[String, DownloadTask] = {
        downloadTaskMap
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
