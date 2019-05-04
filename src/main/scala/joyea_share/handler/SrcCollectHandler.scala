package joyea_share.handler

import com.json.{JsonArray, JsonObject}
import joyea_share.handler.interfaces.{ExecListener, IAction}
import joyea_share.model.SrcCollect
import joyea_share.util.SUtil

import scala.concurrent._
import ExecutionContext.Implicits.global

class SrcCollectHandler extends IAction {
  override def execute(request: JsonObject, listener: ExecListener): Unit = {
    val method = request.get("method").asString()
    method match {
      case "collect" =>
        val srcNeid = request.getLong("neid", -1)
        val sessionUserId = context.sessiono[Long]("user_id").getOrElse(-1L)

        SrcCollect.findByUserIdAndNeid(sessionUserId, srcNeid).onComplete(optionSrcTry => {
          if (optionSrcTry.isSuccess) {
            if (optionSrcTry.get.isDefined) {

            } else {
              SrcCollect.create(
                srcNeid = request.getLong("neid", -1),
                userId = context.sessiono[Long]("user_id").getOrElse(-1),
                srcPath = request.getString("path", ""),
                srcType = request.getString("type", ""),
                srcHash = request.getString("hash", ""),
                srcRev = request.getString("rev", ""),
                srcSize = request.getString("size", "")
              ).onComplete(srcTry => {
                if (srcTry.isSuccess) {
                  listener.onSuccess(respJson = resJson)
                } else {
                  listener.onError("收藏出错:" + SUtil.convertExceptionToStr(srcTry.failed.get))
                }
              })
            }
          } else {
            listener.onError("收藏出错:" + SUtil.convertExceptionToStr(optionSrcTry.failed.get))
          }
        })
      case "unCollect" =>
        SrcCollect.deleteByUserIdAndNeid(
          userId = context.sessiono[Long]("user_id").getOrElse(-1L),
          neid = request.getLong("neid", -1),
        ).onComplete(srcTry => {
          if (srcTry.isSuccess) {
            listener.onSuccess(respJson = resJson)
          } else {
            listener.onError("取消收藏出错:" + SUtil.convertExceptionToStr(srcTry.failed.get))
          }
        })
      case "pageFind" =>
        val curPage = request.getInt("curPage", 1)
        val pageSize = request.getInt("pageSize", 15)
        SrcCollect.pageFindByUserId(
          userId = context.sessiono[Long]("user_id").getOrElse(-1L),
          curPage = request.getInt("curPage", 1),
          pageSize = request.getInt("pageSize", 15)
        ).onComplete(listTry => {
          if (listTry.isSuccess) {
            val collectJsonArr = new JsonArray()
            listTry.get.foreach(collect => {
              collectJsonArr.add(collect.toJson)
            })
            resJson.add("list", collectJsonArr)
            resJson.add("curPage", curPage)
            resJson.add("pageSize", pageSize)
            SrcCollect.countByUserId(userId = context.sessiono[Long]("user_id").getOrElse(-1L)).onComplete(countTry => {
              if (countTry.isSuccess) {
                resJson.add("total", countTry.get)
                listener.onSuccess(respJson = resJson)
              } else {
                listener.onError("查询收藏数出现错误：" + SUtil.convertExceptionToStr(listTry.failed.get))
              }
            })
          } else {
            listener.onError("查询收藏出现错误：" + SUtil.convertExceptionToStr(listTry.failed.get))
          }
        })
      case _ =>
    }
  }
}
