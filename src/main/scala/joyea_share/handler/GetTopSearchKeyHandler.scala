package joyea_share.handler

import com.json.{JsonArray, JsonObject}
import joyea_share.Boot
import joyea_share.handler.interfaces.{ExecListener, IAction}

import scala.collection.JavaConverters


class GetTopSearchKeyHandler extends IAction {
  override def execute(request: JsonObject, listener: ExecListener): Unit = {

    val jArray = new JsonArray()

    Boot.getLastTenTopKey().forEach(value => {
      jArray.add(value)
    })
    resJson.add("data", jArray)
    listener.onSuccess(respJson = resJson)
  }
}
