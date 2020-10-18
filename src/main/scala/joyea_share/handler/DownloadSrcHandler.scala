package joyea_share.handler

import com.json.JsonObject
import joyea_share.handler.interfaces.{ExecListener, IAction}
import joyea_share.module.download.{DownloadRecord, DownloadManager, DownloadTask}
import joyea_share.util.SessionUtil
import scala.collection.JavaConverters._

class DownloadSrcHandler extends IAction {

  override def execute(request: JsonObject, listener: ExecListener): Unit = {

    val src = request.get("src").asArray()
    if (src.isEmpty) {
      listener.onError("待下载资源为空！")
    } else {

      val sessionUserId = context.sessiono[String]("user_id").getOrElse("")

      val downloadRecordList = src.asScala.map(value => {
        val toDownloadObj = value.asObject()
        DownloadRecord(
          index = toDownloadObj.get("index").asInt(),
          joyeaUserId = sessionUserId,
          filePath = toDownloadObj.get("path").asString(),
          rev = toDownloadObj.get("rev").asString(),
          neid = toDownloadObj.get("neid").asString().toLong,
          fileName = toDownloadObj.get("filename").asString().replaceAll("<mark>", "").replaceAll("</mark>", "")
        )
      }).toSeq

      val downloadTask = DownloadTask(
        downloadRoleId = SessionUtil.getUserId(context),
        downloadRoleName = SessionUtil.getUserName(context),
        downloadFile = downloadRecordList
      )

      DownloadManager.doDownload(downloadTask)
      listener.onSuccess(respJson = resJson.add("id", downloadTask.id))
    }
  }
}
