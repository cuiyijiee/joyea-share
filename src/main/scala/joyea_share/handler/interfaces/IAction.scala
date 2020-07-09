package joyea_share.handler.interfaces

import com.json.JsonObject
import xitrum.{Action, Log}

import scala.concurrent.ExecutionContext

trait IAction extends Log{

  val ctx: ExecutionContext = ExecutionContext.Implicits.global

  val resJson = new JsonObject()
  var context: Action = _

  def execute(request: JsonObject, listener: ExecListener): Unit
}

