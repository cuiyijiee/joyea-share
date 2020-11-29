package joyea_share.action.download

import joyea_share.action.BaseAction
import joyea_share.module.download.{DownloadManager, DownloadStatus}
import xitrum.annotation.POST

@POST("/api/v1/download/query")
class QueryDownloadAction extends BaseAction[QueryDownloadReq] {
    override def safeExecute(req: QueryDownloadReq): Unit = {
        baseResponseSuccess(
            DownloadManager.queryTask(req.id)
              .map(item => {
                  DownloadTaskResp(
                      id = item.id,
                      startTime = item.startTime,
                      firstSrcName = item.downloadFile.headOption.map(_.fileName).getOrElse(""),
                      status = item.queryStatus() == DownloadStatus.FINISH
                  )
              })
        )
    }
}

case class QueryDownloadReq(
                             id: String
                           )
