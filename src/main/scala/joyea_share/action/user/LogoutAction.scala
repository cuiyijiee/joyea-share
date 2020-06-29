package joyea_share.action.user

import joyea_share.action.BaseAction
import joyea_share.vo.req.EmptyReq
import xitrum.annotation.POST

@POST("api/v1/user/logout")
class LogoutAction extends BaseAction[EmptyReq] {
  override def safeExecute(req: EmptyReq): Unit = {
    session.remove("user_name")
    session.remove("user_id")
    cyjResponseSuccess(true)
  }
}
