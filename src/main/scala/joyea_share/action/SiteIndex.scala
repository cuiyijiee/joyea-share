package joyea_share.action

import com.json.JsonObject
import xitrum.Action
import xitrum.annotation.GET

@GET("")
class SiteIndex extends Action {
  def execute() {
    respondJsonText(new JsonObject().add("welcome",remoteIp))
  }
}
