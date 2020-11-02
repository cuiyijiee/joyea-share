package joyea_share.action.upload

import joyea_share.action.BaseAction
import joyea_share.model.UploadRecord
import xitrum.annotation.POST

@POST("/api/v1/upload/latest")
class LatestUploadListAction extends BaseAction[LatestUploadReq] {
    override def safeExecute(req: LatestUploadReq): Unit = {
        UploadRecord.latestUploadRecord(req.num)
          .onComplete(safeResponse[Seq[UploadRecord]](_, recordList => {
              baseResponseSuccess(recordList)
          }))
    }
}

case class LatestUploadReq(num: Int = 1000)
