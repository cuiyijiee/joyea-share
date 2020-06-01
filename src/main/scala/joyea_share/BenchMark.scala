package joyea_share

import java.io.File
import java.util.concurrent.Executors

import cats.effect.{Blocker, ContextShift, IO}
import com.ExcelUtil
import com.alibaba.excel.EasyExcelFactory
import joyea_share.db.MySQLSettings
import joyea_share.model.JoyeaUser
import org.http4s.client.{Client, JavaNetClientBuilder}

import scala.concurrent.{Await, ExecutionContext}
import scala.util.{Failure, Success}
import scala.concurrent.duration._

object BenchMark extends MySQLSettings {

  def main(args: Array[String]): Unit = {
    val excelFile = new File("/Users/cuiyijie/Desktop/员工名单.xlsx")
    implicit val ctx = ExecutionContext.global
    ExcelUtil.getExcelData(excelFile, 0).forEach(lineMap => {
      Await.result(JoyeaUser.create(lineMap.get(0), lineMap.get(1), "123456", lineMap.get(2), lineMap.get(3)),5.seconds)
    })
    Thread.sleep(5000)
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
