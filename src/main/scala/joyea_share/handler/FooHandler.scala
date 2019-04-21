package joyea_share.handler

import com.json.JsonObject
import joyea_share.handler.interfaces.{ExecListener, IAction}

class FooHandler extends IAction {
  override def execute(request: JsonObject, listener: ExecListener): Unit = {
    //Thread.sleep(5000)
    resJson.add("hello", "i am foo handler!")
    listener.onSuccess(respJson = resJson)
  }
}
