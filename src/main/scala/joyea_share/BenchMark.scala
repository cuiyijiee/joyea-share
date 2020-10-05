package joyea_share

import java.io.File

import com.json.JsonObject
import joyea_share.util.LenovoUtil

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object BenchMark {

  def main(args: Array[String]): Unit = {

    implicit val ctx: ExecutionContext = ExecutionContext.Implicits.global

    val sessionId = "f8a1fa97b8ae49e8ad444761eadb5d03_346341_696047_meta"
    val file = new File("/Users/cuiyijie/IdeaProjects/joyea_share/websrc/vue.config.js")
    LenovoUtil.preUpload(sessionId, file)
      .onComplete {
        case Failure(exception) =>
          exception.printStackTrace()
        case Success(value) =>
          val json = JsonObject.readFrom(value)
          val region = json.getString("region", "")
          LenovoUtil.uploadFile(sessionId, region, file).onComplete {
            case Failure(exception) =>
              exception.printStackTrace()
            case Success(value) =>
              println(value)
          }
      }

  }

  def test(): Unit = {
    Thread.sleep(2000)
  }
}
