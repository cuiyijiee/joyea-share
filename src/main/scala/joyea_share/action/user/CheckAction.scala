package joyea_share.action.user

import joyea_share.action.BaseAction
import joyea_share.vo.req.EmptyReq
import xitrum.annotation.POST

@POST("api/v1/user/check")
class CheckAction extends BaseAction[EmptyReq] {
  override def safeExecute(req: EmptyReq): Unit = {
    cyjResponseSuccess(sessiono("lenovo_session").isDefined)
  }
}
