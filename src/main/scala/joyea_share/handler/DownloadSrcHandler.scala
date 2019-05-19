package joyea_share.handler

import java.io.File
import java.util.UUID

import com.json.JsonObject
import com.utils.CommonUtil
import joyea_share.handler.interfaces.{ExecListener, IAction}
import joyea_share.util.{CommonListener, LenovoUtil, ZipUtils}

class DownloadSrcHandler extends IAction {

    //超时时间为2分钟，超过时间则下载失败
    private val DOWNLOAD_TIMEOUT = 2 * 60 * 1000

    private val uuid = UUID.randomUUID().toString.replaceAll("\\-", "")

    private var totalDownloadCount = 0
    private var currentDownloadCount = 0

    private val startTime = System.currentTimeMillis()

    override def execute(request: JsonObject, listener: ExecListener): Unit = {

        val src = request.get("src").asArray()
        if (src.isEmpty) {
            listener.onError("待下载资源为空！")
        } else {
            val toDownloadDir = new File(s"${System.getProperty("user.dir")}/tmp/$uuid")
            toDownloadDir.mkdirs()
            totalDownloadCount = src.size()
            val sessionId = context.sessiono[String]("lenovo_session").getOrElse("")
            src.forEach(value => {
                val toDownloadObj = value.asObject()
                val fileName = toDownloadObj.get("filename").asString()
                    .replaceAll("<mark>","")
                    .replaceAll("</mark>","")
                LenovoUtil.downloadFile(
                    sessionId,
                    toDownloadObj.get("rev").asString(),
                    toDownloadObj.get("neid").asString(),
                    toDownloadObj.getString("path_type", "ent"),
                    s"${toDownloadDir.getAbsolutePath}/$fileName",
                    new CommonListener[File] {
                        override def onSuccess(obj: File): Unit = {
                            currentDownloadCount += 1
                        }

                        override def onError(error: String): Unit = {
                            currentDownloadCount += 1
                        }
                    }
                )
            })

            var isTimeOut = false
            while (currentDownloadCount < totalDownloadCount && !isTimeOut) {
                if (System.currentTimeMillis() - startTime > DOWNLOAD_TIMEOUT) { //检测是否超时
                    isTimeOut = true
                }
                Thread.sleep(50)
            }

            if (isTimeOut) {
                listener.onError("下载超时，服务器当前网络状态不佳，建议您稍后再试！")
            } else {
                ZipUtils.compressZip(toDownloadDir.getAbsolutePath, s"${System.getProperty("user.dir")}/tmp/$uuid.zip")
                CommonUtil.delete(toDownloadDir.getAbsolutePath)
                listener.onSuccess(respJson = resJson.add("id", uuid))
            }
        }
    }
}
