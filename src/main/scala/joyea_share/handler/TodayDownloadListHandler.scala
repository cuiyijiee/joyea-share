package joyea_share.handler

import com.json.{JsonArray, JsonObject}
import joyea_share.handler.interfaces.{ExecListener, IAction}
import joyea_share.module.download.DownloadManager
import joyea_share.util.SessionUtil

class TodayDownloadListHandler extends IAction {
    override def execute(request: JsonObject, listener: ExecListener): Unit = {
        val myId = SessionUtil.getUserId(context)

        val myTodayDownload = new JsonArray()
        DownloadManager.getTodayTaskMap().forEach((k, v) => {
            if (v.downloadRoleId == myId) {
                myTodayDownload.add(v.toJson())
            }
        })

        listener.onSuccess(respJson = resJson.add("task", myTodayDownload))
    }
}
