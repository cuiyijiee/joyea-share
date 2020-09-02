package joyea_share.action.upload

import joyea_share.action.BaseAction
import joyea_share.define.ErrorCode
import joyea_share.model.UploadRecord
import joyea_share.module.download.DownloadManager
import joyea_share.service.RedisService
import joyea_share.util.{CommonListener, LenovoUtil}
import xitrum.annotation.POST

@POST("api/v1/upload/manage")
class RecordManageAction extends BaseAction[UploadRecordManageReq] {
  override def safeExecute(req: UploadRecordManageReq): Unit = {
    UploadRecord.find(id = req.recordId)
      .onComplete(safeResponse[Option[UploadRecord]](_, recordOpt => {
        if (recordOpt.isDefined) {
          val record = recordOpt.get
          if (req.allow) {
            LenovoUtil.moveFile(toMoveFileNeid = record.srcNeid, toDirPath = req.uploadPathNeid.get, session = DownloadManager.getAdminToken(),
              listener = new CommonListener[Boolean] {
                override def onSuccess(obj: Boolean): Unit = {
                  UploadRecord.checkRecord(recordId = record.id, req.allow,
                    uploadPath = req.uploadPath, uploadPathNeid = req.uploadPathNeid, srcName = req.srcName)
                    .onComplete(safeResponse[Boolean](_, result => {
                      LenovoUtil.renameFile(record.srcNeid, req.srcName.get, DownloadManager.getAdminToken())
                        .onComplete(safeResponse[Boolean](_, renameResult => {

                          if (req.needCount) {
                            RedisService.recordUpload(record.uploader)
                          }

                          cyjResponseSuccess(result && renameResult)
                        }))
                    }))
                }

                override def onError(error: String): Unit = {
                  cyjResponseError(ErrorCode.unknownError)
                }
              })
          } else {
            UploadRecord.checkRecord(recordId = record.id, allow = req.allow, refuseReason = req.refuseReason,
              uploadPath = req.uploadPath, uploadPathNeid = req.uploadPathNeid, srcName = req.srcName)
              .onComplete(safeResponse[Boolean](_, result => {
                cyjResponseSuccess(result)
              }))
          }
        } else {
          cyjResponseError(ErrorCode.unknownError)
        }
      }))
  }
}

case class UploadRecordManageReq(
                                  recordId: Long,
                                  allow: Boolean,

                                  srcName: Option[String],

                                  uploadPath: Option[String],
                                  uploadPathNeid: Option[Long],

                                  refuseReason: Option[String],

                                  needCount: Boolean = false

                                )