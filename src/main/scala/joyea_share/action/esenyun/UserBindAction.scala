package joyea_share.action.esenyun

import joyea_share.action.BaseAction
import joyea_share.model.{JoyeaUser, NextPlusUser}
import joyea_share.module.download.DownloadManager
import joyea_share.vo.resp.LoginResp
import xitrum.annotation.POST

import scala.util.{Failure, Success}

@POST("api/v1/user/bind")
class UserBindAction extends BaseAction[UserBindReq] {
  override def safeExecute(req: UserBindReq): Unit = {
    NextPlusUser.selectByYtmId(req.ytmId).onComplete {
      case Failure(exception) =>
        logError(exception, "find next plus user error")
      case Success(nextPlusUserOpt) =>
        if (nextPlusUserOpt.isDefined) {
          if (req.isNew) {
            JoyeaUser.create(joyeaId = nextPlusUserOpt.get.phone,
              joyeaName = nextPlusUserOpt.get.name, password = "123456",
              position = nextPlusUserOpt.get.position,
              department = nextPlusUserOpt.get.departmentName, ytmId = req.ytmId).onComplete {
              case Failure(exception) =>
                logError(exception, "create new joyea user error")
              case Success(newJoyeaUser) =>
                session("user_name") = newJoyeaUser.joyeaName
                session("user_id") = newJoyeaUser.joyeaId
                baseResponseSuccess(LoginResp(
                  userName = newJoyeaUser.joyeaName,
                  session = DownloadManager.getAdminToken,
                  isAdmin = newJoyeaUser.isAdmin
                ))
            }
          } else {
            JoyeaUser.findByJoyeaId(req.joyeaUserId.get).onComplete(safeResponse[Option[JoyeaUser]](_, maybeUser => {
              if (maybeUser.isDefined) {
                JoyeaUser.bindYtmId(maybeUser.get.joyeaId, req.ytmId).onComplete(safeResponse[Boolean](_, result => {
                  session("user_name") = maybeUser.get.joyeaName
                  session("user_id") = maybeUser.get.joyeaId
                  baseResponseSuccess(LoginResp(
                    userName = maybeUser.get.joyeaName,
                    session = DownloadManager.getAdminToken,
                    isAdmin = maybeUser.get.isAdmin
                  ))
                }))
              } else {
                baseResponseSuccess(false)
              }
            }))
          }
        } else {
          logErrorStr("next plus user not found:" + req.ytmId)
        }
    }
  }
}

case class UserBindReq(
                        joyeaUserId: Option[String],
                        ytmId: String,
                        isNew: Boolean
                      )


case class UserBindResp(
                         joyeaUser: JoyeaUser
                       )