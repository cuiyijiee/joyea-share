package joyea_share.handler.interfaces

import com.json.JsonObject
import xitrum.Action

trait IAction {
  val resJson = new JsonObject()
  var context: Action = _

  def execute(request: JsonObject, listener: ExecListener): Unit
}

