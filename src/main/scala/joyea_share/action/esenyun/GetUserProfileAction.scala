package joyea_share.action.esenyun

import joyea_share.action.BaseAction
import joyea_share.util.{EsenUserProfile, EsenyunUtil}
import xitrum.annotation.POST

@POST("api/v1/esunyun/profile")
class GetUserProfileAction extends BaseAction[GetUserProfileReq] {
  override def safeExecute(req: GetUserProfileReq): Unit = {
    EsenyunUtil.getUserProfile(req.authCode).onComplete(safeResponse[EsenUserProfile](_, resp => {
      baseResponseSuccess(resp)
    }))
  }
}

case class GetUserProfileReq(
                              authCode: String
                            )
