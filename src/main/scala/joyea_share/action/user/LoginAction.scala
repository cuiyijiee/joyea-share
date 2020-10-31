package joyea_share.action.user

import java.util.Base64

import joyea_share.action.BaseAction
import joyea_share.define.ErrorCode
import joyea_share.model.JoyeaUser
import joyea_share.module.download.DownloadManager
import joyea_share.vo.req.LoginReq
import joyea_share.vo.resp.LoginResp
import xitrum.annotation.POST

import scala.util.{Failure, Success}

@POST("api/v1/user/login")
class LoginAction extends BaseAction[LoginReq] {
  override def safeExecute(req: LoginReq): Unit = {
    JoyeaUser.findByJoyeaId(req.user).onComplete {
      case Success(value) =>
        if (value.isDefined) {
          val pwd = if (req.see.getOrElse(false)) req.pwd else new String(Base64.getDecoder.decode(req.pwd), "utf-8")
          if (value.get.password.equals(pwd)) {
            session("user_name") = value.get.joyeaName
            session("user_id") = value.get.joyeaId
            baseResponseSuccess(LoginResp(
              userName = value.get.joyeaName,
              session = DownloadManager.getAdminToken(),
              isAdmin = value.get.isAdmin
            ))
          } else {
            baseResponseError(ErrorCode.unknownError)
          }
        } else {
          baseResponseError(ErrorCode.unknownError)
        }
      case Failure(exception) =>
        logError(exception)
        baseResponseError(ErrorCode.unknownError)
    }
  }
}
