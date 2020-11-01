package joyea_share.action.upload

import joyea_share.action.BaseAction
import joyea_share.model.UploadRecord
import xitrum.annotation.POST

@POST("/api/v1/upload/yesterday")
class YesterdayUpload extends BaseAction[YesterdayUploadReq] {
    override def safeExecute(req: YesterdayUploadReq): Unit = {
        UploadRecord.yesterdayUploadRecord()
          .onComplete(safeResponse[Seq[UploadRecord]](_, recordList => {
              baseResponseSuccess(recordList)
          }))
    }
}

case class YesterdayUploadReq()
