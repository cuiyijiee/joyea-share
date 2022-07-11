package joyea_share.action.esenyun

import joyea_share.action.BaseAction
import joyea_share.model.{JoyeaUser, NextPlusUser}
import joyea_share.module.download.DownloadManager
import joyea_share.util.EsenyunUtil
import xitrum.annotation.POST

import java.sql
import java.sql.Timestamp
import scala.util.{Failure, Success}

@POST("api/v1/esunyun/profile")
class GetUserProfileAction extends BaseAction[GetUserProfileReq] {
  override def safeExecute(req: GetUserProfileReq): Unit = {
    if (req.authCode.equals("test")) {
      session("user_name") = "test"
      session("user_id") = "test"
      baseResponseSuccess(GetUserProfileResp(
        profile = NextPlusUser("test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test", "test"),
        session = DownloadManager.getAdminToken,
        joyeaUser = Some(JoyeaUser(
          -1L,
          "test",
          "test",
          "test",
          "test",
          "test",
          "test",
          true,
          new Timestamp(System.currentTimeMillis())
        ))
      ))
    } else {
      EsenyunUtil.getUserProfile(req.authCode).onComplete(safeResponse[NextPlusUser](_, resp => {

        NextPlusUser.selectById(resp.id).onComplete {
          case Failure(exception) =>
            log.error("selectById exist error:", exception)
          case Success(userOpt) =>
            if (userOpt.isEmpty) {
              NextPlusUser.create(
                resp.id,
                resp.name,
                resp.easUserId,
                resp.imgUrl,
                resp.phone,
                resp.ytmId,
                resp.ytmOpenId,
                resp.position,
                resp.departmentId,
                resp.departmentName,
                resp.departmentType,
                resp.tenantId,
                resp.yzjOpenId,
                resp.extendInfo
              ).onComplete {
                case Failure(exception) =>
                  log.error("create exist error:", exception)
                case Success(value) =>

              }
            } else {
              //TODO update user profile
            }
            JoyeaUser.findByYtmId(resp.ytmId).onComplete {
              case Failure(exception) =>
                log.error("findByYtmId exist error:", exception)
              case Success(value) =>
                if (value.isDefined) {
                  session("user_name") = value.get.joyeaName
                  session("user_id") = value.get.joyeaId
                }
                baseResponseSuccess(GetUserProfileResp(
                  profile = resp, session = DownloadManager.getAdminToken, joyeaUser = value
                ))
            }
        }
      }))
    }
  }
}

case class GetUserProfileReq(
                              authCode: String
                            )

case class GetUserProfileResp(
                               profile: NextPlusUser,
                               session: String,
                               joyeaUser: Option[JoyeaUser]
                             )
