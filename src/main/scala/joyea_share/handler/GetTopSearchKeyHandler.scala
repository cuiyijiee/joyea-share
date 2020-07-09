package joyea_share.handler

import com.json.{JsonArray, JsonObject}
import joyea_share.Boot
import joyea_share.handler.interfaces.{ExecListener, IAction}
import joyea_share.service.RedisService

import scala.collection.JavaConverters


class GetTopSearchKeyHandler extends IAction {
  override def execute(request: JsonObject, listener: ExecListener): Unit = {

    val jArray = new JsonArray()

    RedisService.getSearchTopList(10).foreach(topKeyList => {
      topKeyList.foreach(entry => {
        jArray.add(entry._1)
      })
      resJson.add("data", jArray)
      listener.onSuccess(respJson = resJson)
    })(ctx)
  }
}
