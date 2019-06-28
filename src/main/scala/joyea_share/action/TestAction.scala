package joyea_share.action

import com.json.{JsonArray, JsonObject}
import joyea_share.util.Constant
import xitrum.{Action, Config}
import xitrum.annotation.GET

@GET("test")
class TestAction extends Action{
    override def execute(): Unit = {
        val resJson = new JsonObject()

        resJson.add("record",Config.xitrum.cache.get(Constant.DOWNLOAD_RECORD_CACHE_KEY).getOrElse(new JsonArray()).asInstanceOf[JsonArray])

        respondJsonText(resJson.toString())
    }
}
