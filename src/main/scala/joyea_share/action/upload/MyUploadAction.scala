package joyea_share.action.upload

import joyea_share.action.BaseAction
import joyea_share.model.UploadRecord
import xitrum.annotation.POST

@POST("api/v1/upload/mime")
class MyUploadAction extends BaseAction[MyUploadReq] {
  override def safeExecute(req: MyUploadReq): Unit = {
    UploadRecord.findAllByUid(myUid).onComplete(safeResponse[List[UploadRecord]](_, resp => {
      baseResponseSuccess(resp)
    }))
  }
}

case class MyUploadReq()
