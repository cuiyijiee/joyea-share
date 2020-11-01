package joyea_share.handler

import com.json.{JsonArray, JsonObject, WriterConfig}
import joyea_share.db.MySQLSettings
import joyea_share.handler.interfaces.{ExecListener, IAction}
import joyea_share.model.{AlbumSrc, SrcCollect}
import joyea_share.module.download.DownloadManager
import joyea_share.service.RedisService
import joyea_share.util.{CommonListener, LenovoUtil}
import scalikejdbc.async.AsyncDB

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class SearchHandler extends IAction {

    override def execute(request: JsonObject, listener: ExecListener): Unit = {
        val sessionUserId = context.sessiono[String]("user_id").getOrElse("")
        val searchKey = request.getString("searchKey", "")
        val offset = request.getLong("offset", 0)

        RedisService.recordSearch(searchKey).foreach(result => {
            log.info(s"record search key $searchKey with result $result")
        })(ctx)

        LenovoUtil.newFtsSearch(DownloadManager.getAdminToken, searchKey = searchKey, searchType = "", offset = offset, new CommonListener[JsonObject] {
            override def onSuccess(obj: JsonObject): Unit = {
                val searchResultValue = obj.get("content")
                if (searchResultValue != null && searchResultValue.isArray) {
                    resJson.add("has_more", obj.getBoolean("has_more", false))
                    resJson.add("next_offset", obj.getLong("next_offset", 0))
                    val contentJsonArr = new JsonArray()
                    searchResultValue.asArray().forEach(value => {
                        val itemJson = value.asObject()
                        //如果客户端搜索的类型是全部，则需要进一步判断一下
                        log.info(itemJson.toString(WriterConfig.PRETTY_PRINT))
                        if (!itemJson.getString("path", "").contains("素材库上传临时文件夹")) {
                            contentJsonArr.add(itemJson)
                        }
                    })

                    //查询资源是否已经收藏
                    contentJsonArr.forEach(contentValue => {
                        val content = contentValue.asObject()
                        val srcNeid = content.getLong("neid", -1)
                        val optionSrc = Await.result(AsyncDB.withPool {
                            implicit tx => {
                                SrcCollect.findByUserIdAndNeid(userId = sessionUserId, neid = srcNeid)
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

                        //转换tag格式，原格式为字符串
                        val tagsStrOpt = content.get("tags")
                        if (tagsStrOpt != null) {
                            val tagArr = new JsonArray()
                            tagsStrOpt.asString().split(8197.asInstanceOf[Char]).foreach(str => {
                                tagArr.add(str)
                            })
                            content.set("tags", tagArr)
                        }
                        val downloadNum = Await.result(RedisService.findFileDownloadNum(srcNeid.toString), Duration.Inf)
                        content.add("download_num", downloadNum)
                        val refNum = Await.result(AlbumSrc.countByNeid(srcNeid), Duration.Inf)
                        content.add("ref_num", refNum)
                    })
                    resJson.add("content", contentJsonArr)
                    listener.onSuccess(respJson = resJson)
                } else {
                    listener.onError("搜索失败：" + obj.toString(WriterConfig.PRETTY_PRINT))
                }
            }

            override def onError(error: String): Unit = {
                listener.onError(error)
            }
        })
    }
}
