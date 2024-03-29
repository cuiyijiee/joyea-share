package joyea_share.handler

import com.json.{JsonArray, JsonObject, WriterConfig}
import joyea_share.handler.interfaces.{ExecListener, IAction}
import joyea_share.model.{AlbumSrc, DirWord}
import joyea_share.module.download.DownloadManager
import joyea_share.service.RedisService
import joyea_share.util.{CommonListener, LenovoUtil}

import java.util
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.jdk.CollectionConverters.IterableHasAsScala

class ListLenovoDirHandler extends IAction {
  override def execute(request: JsonObject, listener: ExecListener): Unit = {
    val path = request.getString("path", "/")
    val pathType = request.getString("path_type", "ent")
    LenovoUtil.listDir(DownloadManager.getAdminToken, path, pathType, new CommonListener[JsonObject] {
      override def onSuccess(obj: JsonObject): Unit = {
        val contentOpt = obj.get("content")
        val neidList = new util.ArrayList[Long]()
        val tempMap = new util.HashMap[String, JsonObject]()
        if (contentOpt != null) {
          val contentJsonArr = contentOpt.asArray()
          val filteredContent = contentJsonArr.asScala.filter(value => {
            val content = value.asObject()
            val path = content.get("path").asString()
            !path.contains("素材库上传临时文件夹")
          })
          filteredContent.foreach(value => {
            val content = value.asObject()
            //创建文件名和json的映射
            val path = content.get("path").asString()
            val fileName = path.substring(path.lastIndexOf("/") + 1)
            tempMap.put(fileName, content)
            val srcNeidValue = content.get("neid")
            var srcNeid = -1L
            if (srcNeidValue.isString) {
              srcNeid = srcNeidValue.asString().toLong
            } else {
              srcNeid = srcNeidValue.asLong()
            }
            content.set("neid", srcNeid)
            //记录neid，用于请求获取tag
            neidList.add(srcNeid)

            //查询数据库获取收藏次数
            val optionSrc = None;
            //                        val optionSrc = Await.result(AsyncDB.withPool {
            //                            implicit tx => {
            //                                SrcCollect.findByUserIdAndNeid(userId = SessionUtil.getUserId(context), neid = srcNeid)
            //                            }
            //                        }, MySQLSettings.MYSQL_READ_TIMEOUT)
            val descArr = new JsonArray()
            //                        Await.result(AsyncDB.withPool {
            //                            implicit tx =>
            //                                AlbumSrc.findByNeid(srcNeid)
            //                        }, MySQLSettings.MYSQL_READ_TIMEOUT).foreach(src => {
            //                            if (src.srcDesc != null && src.srcDesc.trim.length > 0) {
            //                                descArr.add(src.toJson)
            //                            }
            //                        })
            content.add("desc", descArr)
            content.add("collect", optionSrc.isDefined)
            val downloadNum = Await.result(RedisService.findFileDownloadNum(srcNeid.toString), Duration.Inf)
            content.add("download_num", downloadNum)
            val refNum = Await.result(AlbumSrc.countByNeid(srcNeid), Duration.Inf)
            content.add("ref_num", refNum)
          })
          LenovoUtil.getExtraMeta(DownloadManager.getAdminToken, neidList, new CommonListener[JsonObject] {
            override def onSuccess(metaJson: JsonObject): Unit = {
              filteredContent.foreach(contentValue => {
                val content = contentValue.asObject()
                val srcNeid = content.getLong("neid", -1)
                val metaData = metaJson.get(srcNeid.toString).asObject()
                val tagJsonArr = metaData.get("tags").asArray()
                if (tagJsonArr.size() != 0) {
                  val handledTagArr = new JsonArray()
                  metaData.get("tags").asArray().forEach(value => {
                    handledTagArr.add(value.asObject().get("name").asString())
                  })
                  content.add("tags", handledTagArr)
                }
              })
              //尝试进行排序，如果跑出异常则不进行排序
              try {
                val keySet = tempMap.keySet()
                val keyArr = keySet.toArray()
                util.Arrays.sort(keyArr)
                val newContentArr = new JsonArray()
                //增加小白板
                val dirWordList = Await.result(DirWord.findByNeid(obj.get("neid").asString()),Duration.Inf)
                dirWordList.foreach(word => {
                  val wordJson = new JsonObject()
                  wordJson.add("neid",word.wordId)
                  wordJson.add("path",word.wordName)
                  wordJson.add("mime_type","word")
                  newContentArr.add(wordJson)
                })
                keyArr.foreach(key => {
                  newContentArr.add(tempMap.get(key))
                })
                obj.set("content", newContentArr)
                listener.onSuccess(respJson = resJson.add("data", obj))
              } catch {
                case e: Exception => {
                  log.error("列出文件夹列表失败：",e)
                  listener.onSuccess(respJson = resJson.add("data", obj))
                }
              }
            }

            override def onError(error: String): Unit = {
              listener.onError("搜索失败：" + obj.toString(WriterConfig.PRETTY_PRINT))
            }
          })
        } else {
          listener.onSuccess(respJson = resJson.add("data", obj))
        }
      }

      override def onError(error: String): Unit = {
        listener.onError(error)
      }
    })
  }
}
