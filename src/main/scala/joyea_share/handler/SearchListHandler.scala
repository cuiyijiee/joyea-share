package joyea_share.handler

import com.json.{JsonArray, JsonObject}
import joyea_share.handler.interfaces.{ExecListener, IAction}
import joyea_share.model.Album
import joyea_share.util.SUtil

import scala.concurrent.ExecutionContext.Implicits.global

class SearchListHandler extends IAction {
    override def execute(request: JsonObject, listener: ExecListener): Unit = {

        val searchKey = request.get("searchKey").asString()
        Album.searchByName(searchKey).onComplete(searchTry => {
            if (searchTry.isSuccess) {
                val albumList = searchTry.get
                val albumArr = new JsonArray()
                albumList.foreach(album => {
                    albumArr.add(album.toJson)
                })
                listener.onSuccess(respJson = resJson.add("list", albumArr))
            } else {
                listener.onError("搜索失败：" + SUtil.convertExceptionToStr(searchTry.failed.get))
            }
        })
    }
}
