package joyea_share.action.upload

import joyea_share.action.BaseAction
import joyea_share.model.UploadRecord
import xitrum.annotation.POST

@POST("/api/v1/upload/add")
class CreateUploadAction extends BaseAction[CreateUploadReq] {
  override def safeExecute(req: CreateUploadReq): Unit = {
    UploadRecord.create(uploader = req.uploader,
      tempSrcName = req.tempSrcName, srcNeid = req.srcNeid, srcDesc = req.srcDesc,
      srcType = req.srcType, srcRev = req.srcRev, srcHash = req.srcHash, tags = req.tags)
      .onComplete(safeResponse[UploadRecord](_, resp => {
        cyjResponseSuccess("ok")
      }))
  }
}

case class CreateUploadReq(
                            uploader: String,
                            srcNeid: Long,
                            srcType: String,
                            srcRev: String,
                            srcHash: String,
                            srcDesc: String,
                            tempSrcName: String,
                            tags: Seq[String]
                          )
