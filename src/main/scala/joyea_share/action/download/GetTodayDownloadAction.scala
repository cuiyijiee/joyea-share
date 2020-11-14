package joyea_share.action.download

import java.time.OffsetDateTime

import joyea_share.action.BaseAction
import joyea_share.module.download.{DownloadManager, DownloadStatus}
import xitrum.annotation.POST

@POST("/api/v1/download/today")
class GetTodayDownloadAction extends BaseAction[GetTodayDownloadReq] {
    override def safeExecute(req: GetTodayDownloadReq): Unit = {
        baseResponseSuccess(
            DownloadManager.getTodayTaskMap.map(entry => {
                val task = entry._2
                DownloadTaskResp(
                    id = task.id,
                    startTime = task.startTime,
                    firstSrcName = task.downloadFile.head.fileName,
                    status = DownloadManager.queryTask(task.id) == DownloadStatus.FINISH
                )
            })
        )
    }
}

case class GetTodayDownloadReq()

/*
                            id: task.id,
                            startTime: task.startTime,
                            firstSrcName: firstSrcName.substr(0, firstSrcName.lastIndexOf(".")),
                            status: task.finishTime && task.finishTime.length !== 0,
                            opened: _this.handleQueryRecord(task.id)
 */
case class DownloadTaskResp(
                             id: String,
                             startTime: OffsetDateTime,
                             firstSrcName: String,
                             status: Boolean
                           )
