package joyea_share.action

import java.io.File

import joyea_share.module.download.DownloadManager
import joyea_share.util.SUtil
import org.slf4s.LoggerFactory
import xitrum.{Action, Config, Log}
import xitrum.annotation.GET

@GET("download/:id/:firstSrcName")
class DownloadSrcAction extends Action with Log{

    override def execute(): Unit = {
        try {
            val id = paramo[String]("id").getOrElse("")
            val responseFile = new File(s"${DownloadManager.getBaseSaveFilePath()}/$id.zip")
            if (responseFile.exists()) {
                respondFile(responseFile.getAbsolutePath)
            } else {
                respond404Page()
            }
        } catch {
            case e: Exception =>
                log.error("【 DownloadSrcAction 】:" + SUtil.convertExceptionToStr(e))
                respond404Page()
        }
    }
}
