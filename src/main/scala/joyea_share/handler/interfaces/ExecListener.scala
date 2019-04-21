package joyea_share.handler.interfaces

import com.json.JsonObject

/**
  * Created by ivy on 2019-04-02
  */
trait ExecListener {

  def onSuccess(respJson: JsonObject)

  def onError(error: String)
}
