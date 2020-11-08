package joyea_share

import joyea_share.action.leaderboard.util.LeaderboardUtil
import joyea_share.db.MySQLSettings
import joyea_share.model.Album
import org.json4s.DefaultFormats

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object BenchMark extends MySQLSettings {

    def main(args: Array[String]): Unit = {

        implicit val format: DefaultFormats.type = DefaultFormats
        implicit val session: String = "3d27985df21c42f182e8cc0fbd56ca1e_346341_696047_meta"

        //        UploadRecord.latestUploadRecord(10).onComplete {
        //            case Failure(exception) =>
        //                exception.printStackTrace()
        //            case Success(value) =>
        //                println(value)
        //        }

        //        LenovoUtil.ftsSearch(
        //            "听装", "", 202
        //        ).onComplete {
        //            case Failure(exception) =>
        //                exception.printStackTrace()
        //            case Success(value) =>
        //                val resp = Serialization.read[FtsSearchResp](value)
        //                println(s"current find file: ${resp.content.length},and has more: ${resp.has_more},and next_offset is: ${resp.next_offset}")
        //        }
        //        UploadIntegral.create(7758258,"717",5)
        //          .onComplete {
        //              case Failure(exception) =>
        //                  exception.printStackTrace()
        //              case Success(value) =>
        //                  println(value)
        //          }
        val dateRange = LeaderboardUtil.generateDateRange(2020, 0)
        Album.findSharedWithDateLimit(dateRange._1, dateRange._2)
          .onComplete {
              case Failure(exception) =>
                  exception.printStackTrace()
              case Success(value) =>
                  println(value)
          }

        scala.io.StdIn.readLine()
        //    DownloadRecord.create("","123",123,"12313",LocalDateTime.now()).onComplete {
        //      case Failure(exception) =>
        //        exception.printStackTrace()
        //      case Success(value) =>
        //        println(value)
        //    }

        //    val sessionId = "f8a1fa97b8ae49e8ad444761eadb5d03_346341_696047_meta"
        //    val file = new File("/Users/cuiyijie/IdeaProjects/joyea_share/websrc/vue.config.js")
        //    LenovoUtil.preUpload(sessionId, file)
        //      .onComplete {
        //        case Failure(exception) =>
        //          exception.printStackTrace()
        //        case Success(value) =>
        //          val json = JsonObject.readFrom(value)
        //          val region = json.getString("region", "")
        //          LenovoUtil.uploadFile(sessionId, region, file).onComplete {
        //            case Failure(exception) =>
        //              exception.printStackTrace()
        //            case Success(value) =>
        //              println(value)
        //          }
        //      }

    }

    def test(): Unit = {
        Thread.sleep(2000)
    }
}
