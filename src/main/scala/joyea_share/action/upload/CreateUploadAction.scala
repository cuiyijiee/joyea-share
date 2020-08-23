package joyea_share.action.upload

import joyea_share.action.BaseAction
import joyea_share.model.UploadRecord
import xitrum.annotation.POST

@POST("/api/v1/upload/add")
class CreateUploadAction extends BaseAction[CreateUploadReq] {
  override def safeExecute(req: CreateUploadReq): Unit = {
    UploadRecord.create(uploader = req.uploader,
      srcName = req.srcName, srcNeid = req.srcNeid,srcDesc = req.srcDesc,
      srcType = req.srcType, srcRev = req.srcRev, srcHash = req.srcHash,
      uploadPath = req.uploadPath, uploadPathNeid = req.uploadPathNeid, tags = req.tags)
      .onComplete(safeResponse[UploadRecord](_, resp => {
        cyjResponseSuccess("ok")
      }))
  }
}

case class CreateUploadReq(
                            uploader: String,
                            srcName: String,
                            srcNeid: Long,
                            srcType: String,
                            srcRev: String,
                            srcHash: String,
                            srcDesc: String,
                            uploadPath: String,
                            uploadPathNeid: Long,
                            tags: Seq[String]
                          )
