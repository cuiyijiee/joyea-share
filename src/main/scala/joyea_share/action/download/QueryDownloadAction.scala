package joyea_share.action.download

import joyea_share.action.BaseAction
import joyea_share.module.download.DownloadManager
import xitrum.annotation.POST

@POST("/api/v1/download/query")
class QueryDownloadAction extends BaseAction[QueryDownloadReq] {
    override def safeExecute(req: QueryDownloadReq): Unit = {
        baseResponseSuccess(DownloadManager.queryTask(req.id))
    }
}

case class QueryDownloadReq(
                             id: String
                           )
