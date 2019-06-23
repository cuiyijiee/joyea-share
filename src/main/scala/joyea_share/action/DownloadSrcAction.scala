package joyea_share.action

import java.io.File

import joyea_share.module.download.DownloadManager
import joyea_share.util.SUtil
import org.slf4s.LoggerFactory
import xitrum.{Action, Config}
import xitrum.annotation.GET

@GET("download/:id")
class DownloadSrcAction extends Action {
    private val _log = LoggerFactory.getLogger(classOf[DownloadSrcAction])

    override def execute(): Unit = {
        try {
            val id = paramo[String]("id").getOrElse("")
            val responseFile = new File(s"${DownloadManager.getBaseSaveFilePath}/$id.zip")
            if (responseFile.exists()) {
                respondFile(responseFile.getAbsolutePath)
            } else {
                respond404Page()
            }
        } catch {
            case e: Exception =>
                _log.error("【 DownloadSrcAction 】:" + SUtil.convertExceptionToStr(e))
                respond404Page()
        }
    }
}
