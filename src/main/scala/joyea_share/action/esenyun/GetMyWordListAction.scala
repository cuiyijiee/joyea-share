package joyea_share.action.esenyun

import joyea_share.action.BaseAction
import joyea_share.model.{JoyeaUser, NextPlusUser}
import joyea_share.util.{EsenWord, EsenyunUtil}
import xitrum.annotation.POST

@POST("api/v1/esunyun/myword")
class GetMyWordListAction extends BaseAction[GetMyWordListReq] {
  override def safeExecute(req: GetMyWordListReq): Unit = {
    JoyeaUser.findByJoyeaId(myUid).foreach(userOpt => {
      if (userOpt.isDefined && userOpt.get.ytmId.nonEmpty) {
        NextPlusUser.selectByYtmId(userOpt.get.ytmId).foreach(nextPlusUserOpt => {
          if (nextPlusUserOpt.nonEmpty) {
            EsenyunUtil.getYtmWordList(nextPlusUserOpt.get.tenantId,nextPlusUserOpt.get.ytmId,req.search).foreach(result => {
              baseResponseSuccess(GetMyWordListResp(data = result.words))
            })
          } else {
            baseResponseSuccess(GetMyWordListResp(data = List()))
          }
        })
      } else {
        baseResponseSuccess(GetMyWordListResp(data = List()))
      }
    })
  }
}

case class GetMyWordListReq(
                             search: Option[String]
                           )

case class GetMyWordListResp(
                              data: List[EsenWord]
                            )
