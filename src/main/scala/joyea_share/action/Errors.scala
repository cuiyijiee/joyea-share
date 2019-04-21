package joyea_share.action

import com.json.JsonObject
import xitrum.Action
import xitrum.annotation.{Error404, Error500}

@Error404
class NotFoundError extends Action {
  def execute() {
    respondJsonText(new JsonObject())
  }
}

@Error500
class ServerError extends Action {
  def execute() {
    respondJsonText(new JsonObject())
  }
}
