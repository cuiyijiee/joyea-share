package joyea_share.handler

import com.json.{JsonArray, JsonObject}
import joyea_share.db.MySQLSettings
import joyea_share.handler.interfaces.{ExecListener, IAction}
import joyea_share.model.{Album, AlbumSrc}
import joyea_share.util.SUtil
import scalikejdbc.async.AsyncDB

import scala.concurrent._
import ExecutionContext.Implicits.global

class AlbumHandler extends IAction {
    override def execute(request: JsonObject, listener: ExecListener): Unit = {
        val method = request.get("method").asString()
        method match {
            case "save" =>
                val listName = request.get("name").asString().trim
                val listSrc = request.get("src").asArray()
                //查询该名称的album是否存在
                val sessionUserId = context.sessiono[Long]("user_id").getOrElse(-1L)
                Album.findByUserIdAndName(sessionUserId, listName).onComplete(albumSelectTry => {
                    if (albumSelectTry.isSuccess) {
                        if (albumSelectTry.get.isEmpty) {
                            Album.create(userId = sessionUserId, albumName = listName, albumDesc = Option("")).onComplete(albumTry => {
                                if (albumTry.isSuccess) {
                                    val album = albumTry.get
                                    var hasError = false
                                    listSrc.forEach(srcValue => {
                                        if (!hasError) {
                                            val src = srcValue.asObject()
                                            AlbumSrc.create(
                                                albumId = album.albumId,
                                                srcNeid = src.getLong("neid", -1L),
                                                srcPath = src.getString("path", ""),
                                                srcSize = src.getString("size", ""),
                                                srcHash = src.getString("hash", ""),
                                                srcRev = src.getString("rev", ""),
                                                srcDesc = src.getString("joyeaDesc", ""),
                                                srcType = src.getString("type", ""),
                                            ).onComplete(insertSrcTry => {
                                                if (insertSrcTry.isFailure) {
                                                    hasError = true
                                                }
                                            })
                                        }
                                    })
                                    if (hasError) {
                                        Album.delete(album.albumId).onComplete(delTry => {
                                            AlbumSrc.delete(album.albumId).onComplete(delTry => {
                                                listener.onError("创建清单失败!")
                                            })
                                        })
                                    } else {
                                        listener.onSuccess(respJson = resJson.add("alnumId", album.albumId))
                                    }
                                } else {
                                    listener.onError("创建清单失败:" + SUtil.convertExceptionToStr(albumTry.failed.get))
                                }
                            })
                        } else {
                            listener.onError("已经存在相同名称的清单！")
                        }
                    } else {
                        listener.onError("查询是否重名失败:" + SUtil.convertExceptionToStr(albumSelectTry.failed.get
                        ))
                    }
                })
            case "list" =>
                val sessionUserId = context.sessiono[Long]("user_id").getOrElse(-1L)
                Album.findByUserId(sessionUserId).onComplete(findTry => {
                    if (findTry.isSuccess) {
                        val albumList = findTry.get
                        val albumArr = new JsonArray()
                        albumList.foreach(album => {
                            val albumJson = album.toJson
                            val albumSrcList = Await.result(AsyncDB.withPool {
                                implicit tx => {
                                    AlbumSrc.findByAlbumId(album.albumId)
                                }
                            }, MySQLSettings.MYSQL_READ_TIMEOUT)

                            val albumSrcArr = new JsonArray()
                            albumSrcList.foreach(albumSrc => {
                                albumSrcArr.add(albumSrc.toJson)
                            })
                            albumJson.add("src", albumSrcArr)
                            albumArr.add(albumJson)
                        })
                        listener.onSuccess(respJson = resJson.add("data", albumArr))
                    } else {
                        listener.onError("查询失败:" + SUtil.convertExceptionToStr(findTry.failed.get))
                    }
                })
            case "delete" =>
                val albumId = request.get("album_id").asLong()
                Album.delete(albumId).onComplete(delTry => {
                    if (delTry.isSuccess) {
                        AlbumSrc.deleteByAlbumId(albumId).onComplete(_ => {
                            listener.onSuccess(respJson = resJson)
                        })
                    } else {
                        listener.onError("删除失败:" + SUtil.convertExceptionToStr(delTry.failed.get))
                    }
                })
            case "share" =>
                val albumId = request.get("album_id").asLong()
                Album.shareAlbum(albumId).onComplete(shareTry => {
                    if (shareTry.isSuccess) {
                        listener.onSuccess(respJson = resJson)
                    } else {
                        listener.onError("分享失败:" + SUtil.convertExceptionToStr(shareTry.failed.get))
                    }
                })
            case "unShare" =>
                val albumId = request.get("album_id").asLong()
                Album.unShared(albumId).onComplete(shareTry => {
                    if (shareTry.isSuccess) {
                        listener.onSuccess(respJson = resJson)
                    } else {
                        listener.onError("取消分享失败:" + SUtil.convertExceptionToStr(shareTry.failed.get))
                    }
                })
            case "shareList" =>
                val albumId = request.get("album_id").asLong()
                Album.findByAlbumId(albumId).onComplete(findTry => {
                    if (findTry.isSuccess) {
                        if (findTry.get.isDefined) {
                            val album = findTry.get.get
                            if (album.shared) {
                                val albumJson = album.toJson
                                val albumSrcArr = new JsonArray()
                                AlbumSrc.findByAlbumId(album.albumId).onComplete(findSrcTry => {
                                    if (findSrcTry.isSuccess) {
                                        findSrcTry.get.foreach(albumSrc => {
                                            albumSrcArr.add(albumSrc.toJson)
                                        })
                                        albumJson.add("src", albumSrcArr)
                                        listener.onSuccess(respJson = resJson.add("album", albumJson))
                                    } else {
                                        listener.onError("获取分享信息出错:" + SUtil.convertExceptionToStr(findSrcTry.failed.get))
                                    }
                                })
                            } else {
                                listener.onError("获取分享信息出错:用户已经取消分享该清单！")
                            }
                        } else {
                            listener.onError("获取分享信息出错:不存在该分享记录！")
                        }
                    } else {
                        listener.onError("获取分享信息出错:" + SUtil.convertExceptionToStr(findTry.failed.get))
                    }
                })
            case _ =>
        }
    }
}
