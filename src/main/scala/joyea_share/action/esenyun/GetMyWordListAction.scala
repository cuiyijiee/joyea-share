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
            EsenyunUtil.getYtmWordList(nextPlusUserOpt.get.tenantId, nextPlusUserOpt.get.ytmId, req.search, req.pageSize, req.pageNum).foreach(result => {
              baseResponseSuccess(GetMyWordListResp(data = result.words,total = result.total_number, pageNum = req.pageNum, pageSize = req.pageSize))
            })
          } else {
            baseResponseSuccess(GetMyWordListResp(data = List(),total = 0, pageNum = req.pageNum, pageSize = req.pageSize))
          }
        })
      } else {
        baseResponseSuccess(GetMyWordListResp(data = List(),total = 0, pageNum = req.pageNum, pageSize = req.pageSize))
      }
    })
  }
}

case class GetMyWordListReq(
                             search: Option[String],
                             pageSize: Int = 10,
                             pageNum: Int = 0
                           )

case class GetMyWordListResp(
                              data: List[EsenWord],
                              total: Int,
                              pageNum: Int,
                              pageSize: Int
                            )
