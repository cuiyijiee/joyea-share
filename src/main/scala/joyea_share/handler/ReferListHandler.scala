package joyea_share.handler

import com.json.JsonObject
import joyea_share.handler.interfaces.{ExecListener, IAction}
import joyea_share.model.Album
import joyea_share.util.SUtil

import scala.concurrent.ExecutionContext.Implicits.global

class ReferListHandler extends IAction {
    override def execute(request: JsonObject, listener: ExecListener): Unit = {
        val album_id = request.get("albumId").asLong()

        Album.addRefer(album_id).onComplete(addTry => {
            if (addTry.isSuccess) {
                listener.onSuccess(respJson = resJson)
            } else {
                listener.onError(SUtil.convertExceptionToStr(addTry.failed.get))
            }
        })
    }
}
