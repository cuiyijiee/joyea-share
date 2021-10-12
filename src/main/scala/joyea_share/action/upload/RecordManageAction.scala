package joyea_share.action.upload

import joyea_share.action.BaseAction
import joyea_share.define.ErrorCode
import joyea_share.model.{UploadIntegral, UploadRecord}
import joyea_share.module.download.DownloadManager
import joyea_share.service.RedisService
import joyea_share.util.{CommonListener, LenovoUtil}
import xitrum.annotation.POST

import scala.util.{Failure, Success}

@POST("api/v1/upload/manage")
class RecordManageAction extends BaseAction[UploadRecordManageReq] {
    override def safeExecute(req: UploadRecordManageReq): Unit = {
        UploadRecord.find(id = req.recordId)
          .onComplete(safeResponse[Option[UploadRecord]](_, recordOpt => {
              if (recordOpt.isDefined) {
                  val record = recordOpt.get
                  if (req.allow) {
                      LenovoUtil.moveFile(toMoveFileNeid = record.srcNeid.get, toDirPath = req.uploadPathNeid.get,
                          session = DownloadManager.getAdminToken, listener = new CommonListener[Boolean] {
                              override def onSuccess(obj: Boolean): Unit = {

                                  UploadRecord.checkRecord(recordId = record.id, req.allow,
                                      uploadPath = req.uploadPath, uploadPathNeid = req.uploadPathNeid, srcName = req.srcName)
                                    .onComplete(safeResponse[Boolean](_, result => {

                                        LenovoUtil.renameFile(record.srcNeid.get, req.srcName.get, DownloadManager.getAdminToken)
                                          .onComplete(safeResponse[Boolean](_, renameResult => {

                                              if (req.needCount) {
                                                  RedisService.recordUpload(record.uploader)
                                              }

                                              UploadIntegral.create(record.srcNeid.get, record.uploader, req.integral).onComplete {
                                                  case Failure(exception) =>
                                                      log.error("record upload integral error: ", exception)
                                                  case Success(value) =>
                                                      log.info(s"record upload integral success: ${value}")
                                              }

                                              baseResponseSuccess(result && renameResult)
                                          }))
                                    }))
                              }

                              override def onError(error: String): Unit = {
                                  baseResponseError(ErrorCode.unknownError)
                              }
                          })
                  } else {
                      UploadRecord.checkRecord(recordId = record.id, allow = req.allow, refuseReason = req.refuseReason,
                          uploadPath = req.uploadPath, uploadPathNeid = req.uploadPathNeid, srcName = req.srcName)
                        .onComplete(safeResponse[Boolean](_, result => {
                            baseResponseSuccess(result)
                        }))
                  }
              } else {
                  baseResponseError(ErrorCode.unknownError)
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

                                  integral: Long = 1, // 管理员分配的上传积分

                                  needCount: Boolean = false

                                )