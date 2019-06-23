package joyea_share.handler

import com.json.JsonObject
import joyea_share.handler.interfaces.{ExecListener, IAction}
import joyea_share.module.download.{DownloadItem, DownloadManager, DownloadTask}
import joyea_share.util.SessionUtil

class DownloadSrcHandler extends IAction {

    override def execute(request: JsonObject, listener: ExecListener): Unit = {

        val src = request.get("src").asArray()
        if (src.isEmpty) {
            listener.onError("待下载资源为空！")
        } else {
            val downloadTask = DownloadTask(downloadRoleId = SessionUtil.getUserId(context), downloadRoleName = SessionUtil.getUserName(context))
            src.forEach(value => {
                val toDownloadObj = value.asObject()
                downloadTask.downloadFile.add(DownloadItem(
                    path = toDownloadObj.get("path").asString(),
                    rev = toDownloadObj.get("rev").asString(),
                    neid = toDownloadObj.get("neid").asString(),
                    fileName = toDownloadObj.get("filename").asString().replaceAll("<mark>", "").replaceAll("</mark>", "")
                ))
            })
            DownloadManager.doDownload(downloadTask)
            listener.onSuccess(respJson = resJson.add("id", downloadTask.id))
        }
    }
}
