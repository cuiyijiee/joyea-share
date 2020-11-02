package joyea_share

import joyea_share.db.MySQLSettings
import joyea_share.model.UploadRecord
import joyea_share.util.{BaseJsonFormat, LenovoUtil}
import joyea_share.vo.lenovo.FtpSearchResp
import org.json4s.jackson.Serialization

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object BenchMark extends MySQLSettings {

  def main(args: Array[String]): Unit = {

    implicit val ctx: ExecutionContext = ExecutionContext.Implicits.global
    implicit val session:String = "2cc84bf21f2740f2888cb7154ee29133_346341_696047_meta"

    UploadRecord.latestUploadRecord(10).onComplete {
      case Failure(exception) =>
        exception.printStackTrace()
      case Success(value) =>
            println(value)
    }
    scala.io.StdIn.readLine()
//    LenovoUtil.ftsSearch(
//      "听装","",202
//    ).onComplete {
//      case Failure(exception) =>
//        exception.printStackTrace()
//      case Success(value) =>
//        val resp = Serialization.read[FtpSearchResp](value)
//        println(s"current find file: ${resp.content.length},and has more: ${resp.has_more},and next_offset is: ${resp.next_offset}")
//    }
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
