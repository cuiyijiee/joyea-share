package joyea_share

import java.io.File
import java.util.concurrent.Executors

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.read.listener.ReadListener;
import joyea_share.model.JoyeaUser;

import cats.effect.{Blocker, ContextShift, IO}
import com.ExcelUtil
import com.alibaba.excel.{EasyExcel, EasyExcelFactory}
import joyea_share.db.MySQLSettings
import joyea_share.model.JoyeaUser
import joyea_share.util.{CommonListener, LenovoUtil}
import org.http4s.client.{Client, JavaNetClientBuilder}

import scala.concurrent.{Await, ExecutionContext}
import scala.util.{Failure, Success}
import scala.concurrent.duration._

object BenchMark extends MySQLSettings {

  def main(args: Array[String]): Unit = {
//    LenovoUtil.downloadFileV2(
//      "34d9293b2a214580be60c055b195df25_346341_1766247_meta",
//      "/信息化需求评级排序及执行反馈/1、素材库/SIS Go Gel功能饮料（格式工厂转码后）.mp4",
//      "1109395002", "ca364ea082a811e99d290017fa00f183", "ent",
//      "/Users/cuiyijie/IdeaProjects/joyea_share/release/SIS Go Gel功能饮料（格式工厂转码后）.mp4", new CommonListener[File] {
//        override def onSuccess(obj: File): Unit = {
//          println("123")
//        }
//
//        override def onError(error: String): Unit = {
//          println("234")
//        }
//      })
//    Thread.sleep(10000)
    createJoyeaUser()
  }

  def createJoyeaUser():Unit = {
    val userStr =
      """周进军-0016-装调资源保障中心-副总裁/部门经理
        |黄国兰-0070-装调资源保障中心-助理
        |史珂-1013-装调资源保障中心-助理
        |吴华军-0022-装调资源保障中心-实验室主管
        |朱建新-0032-装调资源保障中心-项目经理
        |吴黎军-0028-装调资源保障中心-包装发货专员
        |韦春永-0120-装调资源保障中心-STICK线装配组长
        |缪燕-0402-装调资源保障中心-售后服务专员
        |茅敏-0173-装调资源保障中心-项目部主管
        |束叶军-0172-装调资源保障中心-售后组组长
        |芮丹军-0177-装调资源保障中心-听装调试组长
        |潘军华-0193-装调资源保障中心-听装装配组长
        |王月花-0206-装调资源保障中心-辅助组助理
        |刘进-0308-装调资源保障中心-项目经理
        |陈权辉-0245-装调资源保障中心-装调工程师
        |巢方平-0259-装调资源保障中心-装调工程师
        |蒋明良-0353-装调资源保障中心-装调工程师
        |周燕-0270-装调资源保障中心-听装助理
        |徐磊-0304-装调资源保障中心-质量管理员
        |史晓强-0295-装调资源保障中心-装调工程师
        |肖杰-0315-装调资源保障中心-装调工程师
        |马银宝-0431-装调资源保障中心-装调工程师
        |刘翔-0481-装调资源保障中心-装调工程师
        |王伟俊-0415-装调资源保障中心-装调工程师
        |尹晨辉-0347-装调资源保障中心-装调工程师
        |毛星军-0429-装调资源保障中心-装调工程师
        |王宙-0427-装调资源保障中心-装调工程师
        |柴丹辉-0451-装调资源保障中心-装调工程师
        |戎建平-0453-装调资源保障中心-装调工程师
        |蒋海平-0361-装调资源保障中心-装调工程师
        |朱滨-0367-装调资源保障中心-装调工程师
        |王磊-0412-装调资源保障中心-装调工程师
        |魏文明-0472-装调资源保障中心-装调工程师
        |孔小波-0486-装调资源保障中心-装调工程师
        |孙谦-0485-装调资源保障中心-装调工程师
        |胡叶华-0501-装调资源保障中心-装调工程师
        |王秋平-0509-装调资源保障中心-装调工程师
        |陈阳-0533-装调资源保障中心-装调工程师
        |赵炜-0523-装调资源保障中心-装调工程师
        |马列辉-0531-装调资源保障中心-装调工程师
        |马旭华-0534-装调资源保障中心-装调工程师
        |朱浩翔-0518-装调资源保障中心-装调工程师
        |胡丽君-0561-装调资源保障中心-售后问题处理专员
        |袁京军-0063-装调资源保障中心-项目经理
        |傅宁-0564-装调资源保障中心-装调工程师
        |朱杰-0582-装调资源保障中心-装调工程师
        |冷向辉-0620-装调资源保障中心-装调工程师
        |薛凯-0626-装调资源保障中心-装调工程师
        |巢炜华-0646-装调资源保障中心-装调工程师
        |李新潮-0650-装调资源保障中心-装调工程师
        |芦路-0660-装调资源保障中心-装调工程师
        |丁鹏-0698-装调资源保障中心-装调工程师
        |蒋伟-0700-装调资源保障中心-装调工程师
        |郦阳平-0690-装调资源保障中心-装调工程师
        |陈凯-0504-装调资源保障中心-装调工程师
        |孟安-0718-装调资源保障中心-装调工程师
        |蒋辉-0758-装调资源保障中心-装配厂长
        |杨志明-0762-装调资源保障中心-装调工程师
        |唐琪-0810-装调资源保障中心-装调工程师
        |杜超-0818-装调资源保障中心-装调工程师
        |谈涛-0817-装调资源保障中心-装调工程师
        |陈玉涛-0838-装调资源保障中心-装调工程师
        |石磊-0896-装调资源保障中心-装辅机装配组长
        |胡玉洁-0913-装调资源保障中心-装调工程师
        |刘俊杰-0915-装调资源保障中心-装调工程师
        |尤伟波-0920-装调资源保障中心-装调工程师
        |张文浩-0916-装调资源保障中心-装调工程师
        |贺凯-0926-装调资源保障中心-装调工程师
        |郦亚凯-0943-装调资源保障中心-装调工程师
        |马华新-0944-装调资源保障中心-装调工程师
        |赵云杰-0946-装调资源保障中心-项目经理
        |陆沈辉-0956-装调资源保障中心-见习员工
        |刘俊鸿-0957-装调资源保障中心-见习员工
        |张俊豪-0966-装调资源保障中心-见习员工
        |王程-0969-装调资源保障中心-见习员工
        |胡星辰-0981-装调资源保障中心-见习员工
        |刘仁杰-0982-装调资源保障中心-见习员工
        |张超-0986-装调资源保障中心-见习员工
        |苏稳琦-1017-装调资源保障中心-见习员工
        |魏林豪-1018-装调资源保障中心-见习员工
        |张宁-1016-装调资源保障中心-见习员工
        |刘佳贝-1024-装调资源保障中心-见习员工
        |秦浩-1025-装调资源保障中心-见习员工
        |徐杰-1022-装调资源保障中心-见习员工""".stripMargin

    implicit val ctx = ExecutionContext.global

    userStr.split("\n").foreach(line => {
      val entry = line.split("-")
      JoyeaUser.create(entry(1),entry(0),"123456",entry(3),false,entry(2)).onComplete{
        case Success(e) =>
          println(s"Success: ${e}")
        case Failure(e) =>
          println(s"Failure: ${e}")
      }
      Thread.sleep(100)
    })
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
