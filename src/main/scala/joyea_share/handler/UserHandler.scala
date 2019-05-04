package joyea_share.handler

import java.util.Base64

import com.json.JsonObject
import joyea_share.handler.interfaces.{ExecListener, IAction}
import joyea_share.util.{CommonListener, LenovoUtil}

class UserHandler extends IAction {
  override def execute(request: JsonObject, listener: ExecListener): Unit = {
    val method = request.get("method").asString()
    method match {
      case "login" =>
        val userName = request.get("user").asString()
        val canSee = request.getBoolean("see", false)
        val pwd = if (canSee) request.get("pwd").asString() else new String(Base64.getDecoder.decode(request.get("pwd").asString()), "utf-8")
        LenovoUtil.login(userName, pwd, new CommonListener[(String, String, Long)] {
          override def onSuccess(session: (String, String, Long)): Unit = {
            context.session("lenovo_session") = session._1
            context.session("user_name") = session._2
            context.session("user_id") = session._3
            listener.onSuccess(respJson =
              resJson.add("session", session._1)
                .add("user_name", session._2)
            )
          }

          override def onError(error: String): Unit = {
            listener.onError(error)
          }
        })

      case "logout" =>
        val userName = request.get("user").asString()

        LenovoUtil.logout(userName, new CommonListener[String] {
          override def onSuccess(resp: String): Unit = {
            println(resp)
          }

          override def onError(error: String): Unit = {
            listener.onError(error)
          }
        })

        context.session.remove("lenovo_session")
        listener.onSuccess(respJson = resJson)

      case "check" =>

      case _ =>
    }
  }
}
