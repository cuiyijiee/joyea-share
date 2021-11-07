package joyea_share

import joyea_share.db.MySQLSettings
import joyea_share.util.{EsenyunUtil, LenovoUtil}
import joyea_share.vo.lenovo.FtsSearchResp
import org.cuje.lib.net.HttpUtil
import org.json4s.DefaultFormats
import org.json4s.jackson.{JsonMethods, Serialization}

import java.io.File
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

object BenchMark extends MySQLSettings {

  def main(args: Array[String]): Unit = {

    implicit val format: DefaultFormats.type = DefaultFormats

    val value = Await.result(LenovoUtil.login_("develop@joyea.cn", "123qaz"), Duration.Inf)
    val resp = JsonMethods.parse(value)
    val respSession = resp \ "X-LENOVO-SESS-ID"
    implicit val session: String = respSession.values.toString

    EsenyunUtil.getYtmWordList("8a9c8ff35deb9042015dee0ea0bf0011","8a9d3ed05f10a017015f28179ca40023",None).foreach(wordList => {
      println(wordList)
    })
    test()
    //        LenovoUtil.preUpload(session,new File("C:\\Users\\cuiyijie\\Desktop\\2021-08-12仅一SQL确认.txt")).onComplete {
    //            case Failure(exception) =>
    //                exception.printStackTrace()
    //            case Success(value) =>
    //                println(value)
    //        }

//    EsenyunUtil.getTicket().onComplete {
//      case Failure(exception) =>
//        exception.printStackTrace()
//      case Success(value) =>
//        println(value)
//    }
//
//    test()
    //        LenovoUtil.ftsSearch(
    //            "mp4", "", 50
    //        ).onComplete {
    //            case Failure(exception) =>
    //                exception.printStackTrace()
    //            case Success(value) =>
    //                val resp = Serialization.read[FtsSearchResp](value)
    //                println(s"current find file: ${resp.content.length},and has more: ${resp.has_more},and next_offset is: ${resp.next_offset}")
    //                resp.content.foreach(resultFile=> {
    //                    LenovoUtil.getFileStartUrl(
    //                        resultFile.neid,
    //                        resultFile.rev.getOrElse(""),
    //                        resultFile.nsid,
    //                        resultFile.mime_type.getOrElse(""),
    //                        resultFile.hash
    //                    ).onComplete {
    //                        case Failure(exception) =>
    //                            exception.printStackTrace()
    //                        case Success(value) =>
    //                            val startUrl = (JsonMethods.parse(value) \ "stat_url").values.toString
    //                            HttpUtil.get(startUrl,Map()).onComplete {
    //                                case Failure(exception) =>
    //                                    exception.printStackTrace()
    //                                case Success(value) =>
    //                                    val startUrl = (JsonMethods.parse(value) \ "stat_url").values.toString
    //                                    HttpUtil.get(startUrl,Map()).onComplete {
    //                                        case Failure(exception) =>
    //                                            exception.printStackTrace()
    //                                        case Success(value) =>
    //                                            val result = (JsonMethods.parse(value) \ "result").values.toString
    //                                            if(result.equals("converting")){
    //                                                println(s"需要转码:${resultFile.neid} - ${resultFile.filename}")
    //                                            }
    //                                    }
    //                            }
    //                    }
    //                })
    //        }
  }

  def test(): Unit = {
    Thread.sleep(2000)
  }
}
