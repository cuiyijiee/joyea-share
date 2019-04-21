package joyea_share.action

import java.net.URLDecoder

import com.json.JsonObject
import joyea_share.handler.interfaces.{ActionFacade, ExecListener}
import joyea_share.util.SUtil
import org.slf4s.LoggerFactory
import xitrum.{Action, SkipCsrfCheck}
import xitrum.annotation.POST

@POST("api")
class ApiAction extends Action with SkipCsrfCheck {

  private val _log = LoggerFactory.getLogger(classOf[ApiAction])

  override def execute(): Unit = {

    val content = request.content()
    if (content != null) {
      val buff = content.asInstanceOf[io.netty.buffer.UnpooledHeapByteBuf]
      val arr = buff.array()
      if (arr.nonEmpty) {
        val toDecodeStr = new String(arr, "utf-8")
        val toHandleJson = JsonObject.readFrom(URLDecoder.decode(toDecodeStr, "utf-8"))
        val action = toHandleJson.getString("action", "")
        try {
          val methodHandler = ActionFacade.getMethodHandler(action)
          methodHandler.context = this
          methodHandler.execute(toHandleJson, listener = new ExecListener {
            override def onSuccess(respJson: JsonObject): Unit = {
              respJson.set("result", true)
              respondJsonText(respJson.toString())
            }

            override def onError(error: String): Unit = {
              //val errorDetail = SUtil.convertExceptionToStr(exception)
              _log.error(error)
              val errorJson = new JsonObject().add("result", false).add("action", action).add("msg", error)
              respondJsonText(errorJson.toString())
            }
          })
        } catch {
          case exception: ClassNotFoundException =>
            val errorDetail = SUtil.convertExceptionToStr(exception)
            _log.error(errorDetail)
            val errorJson = new JsonObject().add("result", false).add("action", action).add("msg", "未找到该方法：" + action)
            respondJsonText(errorJson.toString())
          case throwable: Throwable =>
            val errorDetail = SUtil.convertExceptionToStr(throwable)
            _log.error(errorDetail)
            val errorJson = new JsonObject().add("result", false).add("action", action).add("msg", errorDetail)
            respondJsonText(errorJson.toString())
        }
      }
    }
  }
}
