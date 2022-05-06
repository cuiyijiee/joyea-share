package joyea_share.action.download

import joyea_share.action.BaseAction
import joyea_share.define.ErrorCode
import joyea_share.model.DownloadRecord
import joyea_share.module.download.{DownloadManager, DownloadTask}
import xitrum.annotation.POST

@POST("/api/v1/download/prepare")
class PrepareDownloadAction extends BaseAction[PrepareDownloadReq] {
    override def safeExecute(req: PrepareDownloadReq): Unit = {
        if (req.src.isEmpty) {
            baseResponseError(ErrorCode.toDownloadListIsEmpty)
        } else {
            val downloadTask = DownloadTask(
                downloadRoleId = myUid,
                downloadRoleName = myName,
                downloadFile = req.src.map(src => {
                    DownloadRecord(
                        index = src.index,
                        filePath = src.path.getOrElse(""),
                        joyeaUserId = myUid,
                        rev = src.rev.getOrElse(""),
                        neid = src.neid,
                        fileName = src.filename
                    )
                })
            )
            DownloadManager.doDownload(downloadTask)
            baseResponseSuccess(downloadTask.id)
        }
    }
}

case class PrepareDownloadReq(
                               src: Seq[DownloadSrc]
                             )

/*
index = toDownloadObj.get("index").asInt(),
          joyeaUserId = sessionUserId,
          filePath = toDownloadObj.get("path").asString(),
          rev = toDownloadObj.get("rev").asString(),
          neid = toDownloadObj.get("neid").asString().toLong,
          fileName = toDownloadObj.get("filename").asString().replaceAll("<mark>", "").replaceAll("</mark>", "")
 */
case class DownloadSrc(
                        index: Int,
                        path: Option[String],
                        rev: Option[String],
                        neid: String,
                        filename: String
                      )
