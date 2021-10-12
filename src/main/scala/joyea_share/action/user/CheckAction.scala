package joyea_share.action.user

import joyea_share.action.BaseAction
import joyea_share.define.ErrorCode
import joyea_share.module.download.DownloadManager
import joyea_share.vo.req.EmptyReq
import xitrum.annotation.POST

@POST("api/v1/user/check")
class CheckAction extends BaseAction[EmptyReq] {
  override def safeExecute(req: EmptyReq): Unit = {
    if (sessiono("user_id").isDefined) {
      baseResponseSuccess(DownloadManager.getAdminToken)
    } else {
      baseResponseError(ErrorCode.userSessionInvalid)
    }
  }
}
