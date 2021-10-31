package joyea_share.action.album

import joyea_share.action.BaseAction
import joyea_share.model.UploadRecord
import xitrum.annotation.POST

@POST("api/src/getDefaultJoyeaDesc")
class GetDefaultJoyeaDescAction extends BaseAction[GetDefaultCommentReq] {
  override def safeExecute(req: GetDefaultCommentReq): Unit = {
    UploadRecord.findByNeid(neid = req.neid).onComplete(safeResponse[Option[UploadRecord]](_, record => {
      if (record.isDefined) {
        baseResponseSuccess(record.get.srcDesc)
      } else {
        baseResponseSuccess("")
      }
    }))
  }
}

case class GetDefaultCommentReq(
                                 neid: String
                               )