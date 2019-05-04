package joyea_share.handler

import com.json.{JsonArray, JsonObject, WriterConfig}
import joyea_share.db.MySQLSettings
import joyea_share.handler.interfaces.{ExecListener, IAction}
import joyea_share.model.SrcCollect
import joyea_share.util.{CommonListener, LenovoUtil}
import scalikejdbc.async.AsyncDB

import scala.concurrent.Await

class SearchHandler extends IAction {

  private val supportType = List("video", "image")

  override def execute(request: JsonObject, listener: ExecListener): Unit = {
    val sessionId = context.sessiono[String]("lenovo_session").getOrElse("")
    val sessionUserId = context.sessiono[Long]("user_id").getOrElse(-1L)
    val searchKey = request.getString("searchKey", "")
    val searchType = request.getString("searchType", "")
    val offset = request.getLong("offset", 0)

    LenovoUtil.ftsSearch(sessionId, searchKey = searchKey, searchType = searchType, offset = offset, new CommonListener[JsonObject] {
      override def onSuccess(obj: JsonObject): Unit = {
        val searchResultValue = obj.get("content")
        if (searchResultValue != null && searchResultValue.isArray) {
          resJson.add("has_more", obj.getBoolean("has_more", false))
          resJson.add("next_offset", obj.getLong("next_offset", 0))
          val contentJsonArr = new JsonArray()
          searchResultValue.asArray().forEach(value => {
            val itemJson = value.asObject()
            //如果客户端搜索的类型是全部，则需要进一步判断一下
            if (searchType.equals("")) {
              //如果是目录或是不支持的文件类型，则过滤
              if (!itemJson.getBoolean("is_dir", true)) {
                val itemType = itemJson.get("mime_type").asString()
                if (supportType.indexOf(itemType.split("/").head) != -1) {
                  contentJsonArr.add(itemJson)
                }
              }
            } else {
              contentJsonArr.add(itemJson)
            }
          })
          //查询资源是否已经收藏
          contentJsonArr.forEach(contentValue => {
            val content = contentValue.asObject()
            val optionSrc = Await.result(AsyncDB.withPool {
              implicit tx => {
                SrcCollect.findByUserIdAndNeid(userId = sessionUserId, neid = content.getLong("neid", -1))
              }
            }, MySQLSettings.MYSQL_READ_TIMEOUT)
            content.add("collect", optionSrc.isDefined)
          })
          resJson.add("content", contentJsonArr)
          listener.onSuccess(respJson = resJson)
        } else {
          listener.onError("搜索失败：" + obj.toString(WriterConfig.PRETTY_PRINT))
        }
      }

      override def onError(error: String): Unit = {
        listener.onError(error)
      }
    })
  }
}
