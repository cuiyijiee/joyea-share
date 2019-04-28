package joyea_share.handler

import com.json.JsonObject
import joyea_share.handler.interfaces.{ExecListener, IAction}

class SrcLikeHandler extends IAction {
  override def execute(request: JsonObject, listener: ExecListener): Unit = {
    val method = request.get("method").asString()
    method match {
      case "like" =>

      case _ =>
    }
  }
}
