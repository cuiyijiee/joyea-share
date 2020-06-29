package joyea_share

import java.io.File
import java.util.concurrent.Executors

import cats.effect.{Blocker, ContextShift, IO}
import com.ExcelUtil
import com.alibaba.excel.EasyExcelFactory
import joyea_share.db.MySQLSettings
import joyea_share.model.JoyeaUser
import joyea_share.util.{CommonListener, LenovoUtil}
import org.http4s.client.{Client, JavaNetClientBuilder}

import scala.concurrent.{Await, ExecutionContext}
import scala.util.{Failure, Success}
import scala.concurrent.duration._

object BenchMark {

  def main(args: Array[String]): Unit = {
    LenovoUtil.downloadFileV2(
      "34d9293b2a214580be60c055b195df25_346341_1766247_meta",
      "/信息化需求评级排序及执行反馈/1、素材库/SIS Go Gel功能饮料（格式工厂转码后）.mp4",
      "1109395002", "ca364ea082a811e99d290017fa00f183", "ent",
      "/Users/cuiyijie/IdeaProjects/joyea_share/release/SIS Go Gel功能饮料（格式工厂转码后）.mp4", new CommonListener[File] {
        override def onSuccess(obj: File): Unit = {
          println("123")
        }

        override def onError(error: String): Unit = {
          println("234")
        }
      })
    Thread.sleep(10000)
  }

  def test(): Unit = {


    Thread.sleep(2000)
  }

  def testNewClient(): Unit = {
    implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
    val blockingPool = Executors.newFixedThreadPool(5)
    val blocker = Blocker.liftExecutorService(blockingPool)
    val httpClient: Client[IO] = JavaNetClientBuilder[IO](blocker).create

    val request = httpClient.expect[String]("https://www.baidu.com")
    request.unsafeRunAsync {
      case Left(value) =>
        value.printStackTrace()
      case Right(value) =>
        println(value)
    }
  }
}
