package joyea_share.handler

import com.json.JsonObject
import joyea_share.handler.interfaces.{ExecListener, IAction}
import joyea_share.module.download.{DownloadManager, DownloadStatus}

class QueryDownloadHandler extends IAction {
    override def execute(request: JsonObject, listener: ExecListener): Unit = {
        val taskId = request.get("id").asString()
        val status = DownloadManager.queryTask(taskId)
        if (status == DownloadStatus.NONE) {
            listener.onError("没找到该下载任务！")
        } else {
            listener.onSuccess(respJson = resJson.add("done", status == DownloadStatus.FINISH))
        }
    }
}
