package joyea_share.action.user

import joyea_share.action.BaseAction
import joyea_share.model.JoyeaUser
import xitrum.annotation.POST

@POST("api/v1/user/changePwd")
class ChangePwdAction extends BaseAction[ChangePwdReq] {
  override def safeExecute(req: ChangePwdReq): Unit = {
    JoyeaUser.findByJoyeaId(myUid).onComplete(safeResponse[Option[JoyeaUser]](_, maybeUser => {
      if (maybeUser.isDefined) {
        if (maybeUser.get.password.equals(req.curPwd)) {
          JoyeaUser.changePwd(maybeUser.get.id, req.newPwd).onComplete(safeResponse[Boolean](_, result => {
            cyjResponseSuccess(result)
          }))
        } else {
          cyjResponseSuccess(false)
        }
      } else {
        cyjResponseSuccess(false)
      }
    }))
  }
}

case class ChangePwdReq(
                         curPwd: String,
                         newPwd: String
                       )