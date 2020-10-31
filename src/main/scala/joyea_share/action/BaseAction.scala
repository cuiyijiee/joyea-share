package joyea_share.action

import joyea_share.define.ErrorCode
import joyea_share.util.BaseJsonFormat
import joyea_share.vo.BaseResp
import org.json4s.jackson.Serialization
import xitrum.{Action, Log, SkipCsrfCheck}

import scala.util.{Failure, Success, Try}


class UserNotAuthException extends RuntimeException {

}

abstract class BaseAction[T: Manifest] extends Action with SkipCsrfCheck with Log with BaseJsonFormat {

  override def execute(): Unit = {
    try {
      log.error("request: " + requestContentString)
      val request = Serialization.read[T](requestContentString)
      safeExecute(req = request)
    } catch {
      case e: UserNotAuthException =>
        baseResponseError(ErrorCode.userSessionInvalid)
      case e: Throwable =>
        e.printStackTrace()
        baseResponseError(ErrorCode.unknownError)
    }
  }

  def myUid: String = {
    val uidOpt = sessiono[String]("user_id")
    if (uidOpt.isEmpty) {
      throw new UserNotAuthException()
    }
    uidOpt.get
  }

  def myName: String = {
    val nameOpt = sessiono[String]("user_name")
    if (nameOpt.isEmpty) {
      throw new UserNotAuthException()
    }
    nameOpt.get
  }

  def safeExecute(req: T): Unit

  def safeResponse[U](serverRespTry: Try[U], result: U => Unit): Unit = {
    serverRespTry match {
      case Success(value) =>
        result(value)
      case Failure(exception) =>
        log.error("query database error:",exception)
        baseResponseError(ErrorCode.dbExecuteError)
    }
  }


  def baseResponseSuccess(data: Any): Unit = {
    log.error("response success: " + data)
    respondJsonText(Serialization.write(BaseResp(2000, data)))
  }

  def baseResponseError(code: Int): Unit = {
    log.error("response error: " + code)
    respondJsonText(Serialization.write(BaseResp(code, null)))
  }

  def logError(exception: Throwable, reason: String = ""): Unit = {
    log.error(s"$reason error:", exception)
  }

}
