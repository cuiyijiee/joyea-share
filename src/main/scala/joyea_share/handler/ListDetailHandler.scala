package joyea_share.handler

import com.json.{JsonArray, JsonObject}
import joyea_share.handler.interfaces.{ExecListener, IAction}
import joyea_share.model.AlbumSrc
import joyea_share.util.SUtil

import scala.concurrent.ExecutionContext.Implicits.global

class ListDetailHandler extends IAction {
    override def execute(request: JsonObject, listener: ExecListener): Unit = {

        val albumId = request.get("albumId").asLong()

        AlbumSrc.findByAlbumId(albumId).onComplete(findTry => {
            if (findTry.isSuccess) {
                val listArr = new JsonArray()
                findTry.get.foreach(item => {
                    listArr.add(item.toJson)
                })
                listener.onSuccess(respJson = resJson.add("list", listArr))
            } else {
                listener.onError("查看失败：" + SUtil.convertExceptionToStr(findTry.failed.get))
            }
        })
    }
}
