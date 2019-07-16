package joyea_share.handler

import com.json.{JsonArray, JsonObject}
import joyea_share.db.MySQLSettings
import joyea_share.handler.interfaces.{ExecListener, IAction}
import joyea_share.model.{AlbumSrc, SrcCollect}
import joyea_share.util.{CommonListener, LenovoUtil, SessionUtil}
import scalikejdbc.async.AsyncDB

import scala.concurrent.Await

class ListLenovoDirHandler extends IAction {
    override def execute(request: JsonObject, listener: ExecListener): Unit = {
        val path = request.getString("path", "/")
        val pathType = request.getString("path_type", "ent")
        val sessionId = SessionUtil.getSessionId(context)
        LenovoUtil.listDir(sessionId, path, pathType, new CommonListener[JsonObject] {
            override def onSuccess(obj: JsonObject): Unit = {
                val contentOpt = obj.get("content")
                if (contentOpt != null) {
                    contentOpt.asArray().forEach(value => {
                        val content = value.asObject()
                        val srcNeid = content.getLong("neid", -1)
                        val optionSrc = Await.result(AsyncDB.withPool {
                            implicit tx => {
                                SrcCollect.findByUserIdAndNeid(userId = SessionUtil.getUserId(context), neid = srcNeid)
                            }
                        }, MySQLSettings.MYSQL_READ_TIMEOUT)
                        val usedSrcList = Await.result(AsyncDB.withPool {
                            implicit tx =>
                                AlbumSrc.findByNeid(srcNeid)
                        }, MySQLSettings.MYSQL_READ_TIMEOUT)
                        val descArr = new JsonArray()
                        usedSrcList.foreach(src => {
                            if (src.srcDesc != null && src.srcDesc.trim.length > 0) {
                                descArr.add(src.toJson)
                            }
                        })
                        content.add("desc", descArr)
                        content.add("collect", optionSrc.isDefined)
                    })
                }
                listener.onSuccess(respJson = resJson.add("data", obj))
            }

            override def onError(error: String): Unit = {
                listener.onError(error)
            }
        })
    }
}
