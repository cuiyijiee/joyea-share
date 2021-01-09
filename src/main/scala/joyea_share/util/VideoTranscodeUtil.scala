package joyea_share.util

import com.utils.CommonUtil
import joyea_share.vo.lenovo.{FtsSearchContent, FtsSearchResp}
import org.cuje.lib.TimeUtil
import org.cuje.lib.net.HttpUtil
import org.json4s.jackson.{JsonMethods, Serialization}
import org.json4s.{DefaultFormats, JValue}
import xitrum.Log

import java.io.File
import java.util
import java.util.{Timer, TimerTask}
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

/**
  * @author cyj976655@gmail.com
  * @date 2021/1/3 14:30
  */

object VideoTranscodeUtil {

  val videoSuffixArr: Array[String] = Array("mpg", "mpeg", "mp4", "wmv", "avi", "mov", "3gp", "flv", "rmvb", "webm")
  def startJob(): Unit = {
    videoSuffixArr.foreach(videoSuffix => {
      val suffixVideo = new SuffixVideoTranscodeUtil(videoSuffix)
      suffixVideo.doJob()
    })
  }
}

class SuffixVideoTranscodeUtil(videoSuffix: String) extends Log {

  implicit val format: DefaultFormats.type = DefaultFormats

  val transcodingMap = new util.HashMap[Long, FtsSearchContent]()
  val timer = new Timer()

  var currentOffset = 0
  val value: String = Await.result(LenovoUtil.login_("phdycn@joyea.cn", "1234567890@joyea"), Duration.Inf)
  val resp: JValue = JsonMethods.parse(value)
  val respSession: JValue = resp \ "X-LENOVO-SESS-ID"
  implicit val session: String = respSession.values.toString

  val fileLogSb: StringBuffer = new StringBuffer()

  def doJob(): Unit = {
    fileLogSb.append(s"neid,filename,filesize,status,date\n")
    _doJob()
  }

  def _doJob(): Unit = {
    //implicit val session: String = DownloadManager.getAdminToken
    LenovoUtil.ftsSearch(
      videoSuffix, "", currentOffset
    ).onComplete {
      case Failure(exception) =>
        exception.printStackTrace()
      case Success(value) =>
        val resp = Serialization.read[FtsSearchResp](value)
        log.info(s"current find file: ${resp.content.length},and has more: ${resp.has_more},and next_offset is: ${resp.next_offset}")
        if (resp.has_more) {
          resp.content.foreach(content => {
            if(content.filename.replaceAll("<mark>","").replaceAll("</mark>","").endsWith(videoSuffix)){
              transcodingMap.put(content.neid, content)
            }
          })
          currentOffset = resp.next_offset
          _doJob()
        } else {
          log.info(s"find all video 【${videoSuffix}】")
          checkTransCodingStatus()
        }
    }
  }

  def doLogFileStatus(content: FtsSearchContent, status: String): Unit = this.synchronized{
    log.info(s"do log:${content} - ${status}")
    fileLogSb.append(s"${content.neid},${content.filename},${content.size},${status},${TimeUtil.getCurrentDateStr()}\n")
  }

  def finishCheck(): Unit = {
    new File(s"./video-transcoding/${TimeUtil.getCurrentDateStr("yyyy_MM_dd")}").mkdirs()
    CommonUtil.writeFile(s"./video-transcoding/${TimeUtil.getCurrentDateStr("yyyy_MM_dd")}/${TimeUtil.getCurrentDateStr("HH_mm")}_${videoSuffix}.csv", fileLogSb.toString)
  }

  /**
    * 60秒检测一次是否完成
    */
  def checkTransCodingStatus(): Unit = {
    timer.schedule(new TimerTask {
      override def run(): Unit = {
        _checkTransCodingStatus()
      }
    }, 60 * 1000)
  }

  def _checkTransCodingStatus(): Unit = {
    if (transcodingMap.isEmpty) {
      log.info("检查完成")
      finishCheck()
      return
    } else {
      log.info(s"还有【${transcodingMap.size()}】个文件需要检查")
    }
    checkTransCodingStatus()
    transcodingMap.forEach((neid, resultFile) => {
      log.info(s"开始检查转码:${resultFile}")
      LenovoUtil.getFileStartUrl(
        resultFile.neid,
        resultFile.rev.getOrElse(""),
        resultFile.nsid,
        resultFile.mime_type.getOrElse(""),
        resultFile.hash
      ).onComplete {
        case Failure(exception) =>
          exception.printStackTrace()
        case Success(value) =>
          val startUrl = (JsonMethods.parse(value) \ "stat_url").values.toString
          HttpUtil.get(startUrl, Map()).onComplete {
            case Failure(exception) =>
              exception.printStackTrace()
            case Success(value) =>
              val startUrl = (JsonMethods.parse(value) \ "stat_url").values.toString
              HttpUtil.get(startUrl, Map()).onComplete {
                case Failure(exception) =>
                  exception.printStackTrace()
                case Success(value) =>
                  val result = (JsonMethods.parse(value) \ "result").values.toString
                  if (result.equals("converting")) {
                    if (!transcodingMap.containsKey(resultFile.neid)) {
                      transcodingMap.put(resultFile.neid, resultFile)
                      doLogFileStatus(resultFile, "正在转码")
                      log.info(s"需要转码:${resultFile.neid} - ${resultFile.filename}")
                    }
                  } else {
                    if (transcodingMap.containsKey(resultFile.neid)) {
                      transcodingMap.remove(resultFile.neid)
                      doLogFileStatus(resultFile, "转码完成")
                      log.info(s"无需转码:${resultFile.neid} - ${resultFile.filename}")
                    }
                  }
              }
          }
      }
    })
  }
}