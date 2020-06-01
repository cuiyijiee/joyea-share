package joyea_share.action

import joyea_share.define.ErrorCode
import joyea_share.util.BaseJsonFormat
import joyea_share.vo.BaseResp
import org.json4s.jackson.Serialization
import xitrum.{Action, Log, SkipCsrfCheck}

abstract class BaseAction[T: Manifest] extends Action with SkipCsrfCheck with Log with BaseJsonFormat {

  override def execute(): Unit = {
    try {
      val request = Serialization.read[T](requestContentString)
      safeExecute(req = request)
    } catch {
      case e: Throwable =>
        e.printStackTrace()
        cyjResponseError(ErrorCode.unknownError)
    }
  }

  def safeExecute(req: T): Unit

  def cyjResponseSuccess(data: Any): Unit = {
    respondJsonText(Serialization.write(BaseResp(2000, data)))
  }

  def cyjResponseError(code: Int): Unit = {
    respondJsonText(Serialization.write(BaseResp(code, null)))
  }

  def logError(exception: Throwable, reason: String = ""): Unit = {
    log.error(s"$reason error:", exception)
  }

}
