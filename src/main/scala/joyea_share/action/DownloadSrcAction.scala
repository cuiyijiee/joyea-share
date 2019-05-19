package joyea_share.action

import java.io.File

import joyea_share.util.SUtil
import org.slf4s.LoggerFactory
import xitrum.Action
import xitrum.annotation.GET

@GET("download/:id")
class DownloadSrcAction extends Action {
    private val _log = LoggerFactory.getLogger(classOf[DownloadSrcAction])

    override def execute(): Unit = {
        try{
            val id = paramo[String]("id").getOrElse("")
            val toDownloadFile = new File(s"${System.getProperty("user.dir")}/tmp/$id.zip")
            if (toDownloadFile.exists()) {
                respondFile(toDownloadFile.getAbsolutePath)
            } else {
                respond404Page()
            }
        }catch {
            case e:Exception =>
                _log.error("【 DownloadSrcAction 】:" + SUtil.convertExceptionToStr(e))
                respond404Page()
        }
    }
}
