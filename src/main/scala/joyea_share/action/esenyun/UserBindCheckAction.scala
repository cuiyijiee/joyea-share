package joyea_share.action.esenyun

import joyea_share.action.BaseAction
import joyea_share.define.ErrorCode
import joyea_share.model.{JoyeaUser, NextPlusUser}
import xitrum.annotation.POST

import scala.util.{Failure, Success}

@POST("api/v1/user/bind/check")
class UserBindCheckAction extends BaseAction[UserBindCheckReq] {
  override def safeExecute(req: UserBindCheckReq): Unit = {
    JoyeaUser.findByJoyeaId(req.user).onComplete {
      case Success(joyeaUser) =>
        if (joyeaUser.isDefined) {
          //校验密码是否通过
          if (joyeaUser.get.password.equals(req.pwd)) {
            if (joyeaUser.get.ytmId == null || joyeaUser.get.ytmId.isEmpty) {
              NextPlusUser.selectByYtmId(req.ytmId).onComplete {
                case Failure(exception) =>
                  logError(exception, "next user not exist")
                  baseResponseError(ErrorCode.unknownError)
                case Success(nextPlusUser) =>
                  if (nextPlusUser.isDefined) {
                    baseResponseSuccess(UserBindCheckResponse(
                      nextPlusUser = nextPlusUser.get,
                      joyeaUser = joyeaUser.get
                    ))
                  }else{
                    logErrorStr(reason = "nextplus user not exist ：" + req.ytmId)
                    baseResponseError(ErrorCode.unknownError)
                  }
              }
            } else {
              logErrorStr(reason = "user has been binded ：" + req.user)
              baseResponseError(ErrorCode.unknownError)
            }
          } else {
            logErrorStr(reason = "joyea user pwd now correct：" + req.user)
            baseResponseError(ErrorCode.unknownError)
          }
        } else {
          logErrorStr(reason = "joyea user not exist：" + req.user)
          baseResponseError(ErrorCode.unknownError)
        }
      case Failure(exception) =>
        logError(exception, "find joyea user failed")
        baseResponseError(ErrorCode.unknownError)
    }
  }
}

case class UserBindCheckReq(
                             user: String,
                             pwd: String,
                             ytmId: String
                           )

case class UserBindCheckResponse(
                                  nextPlusUser: NextPlusUser,
                                  joyeaUser: JoyeaUser
                                )