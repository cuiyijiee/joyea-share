package joyea_share.action.album

import java.sql.Timestamp

import joyea_share.action.BaseAction
import joyea_share.db.MySQLSettings
import joyea_share.model.{Album, AlbumSrc}
import joyea_share.vo.req.PageListAlbumReq
import scalikejdbc.async.AsyncDB
import xitrum.annotation.POST

import scala.concurrent.Await


@POST("api/v1/album/pageList")
class PageListAction extends BaseAction[PageListAlbumReq] {
    override def safeExecute(req: PageListAlbumReq): Unit = {
        Album.pageListAlbum(req.curPage, req.pageSize).onComplete(safeResponse[List[Album]](_, result => {
            cyjResponseSuccess(result.map(album => {
                PageListAlbumResp(
                    albumId = album.albumId,
                    userId = album.userId,
                    userName = album.userName,
                    albumName = album.albumName,
                    albumDesc = album.albumDesc,
                    shared = album.shared,
                    createdAt = album.createdAt,
                    referNum = album.referNum,
                    updatedAt = album.updatedAt,
                    srcList = Await.result(AsyncDB.withPool {
                        implicit tx => {
                            AlbumSrc.findByAlbumId(album.albumId)
                        }
                    }, MySQLSettings.MYSQL_READ_TIMEOUT),
                )
            }))
        }))
    }
}

case class PageListAlbumResp(
                              albumId: Long,
                              userId: String,
                              userName: String,
                              albumName: String,
                              albumDesc: Option[String],
                              shared: Boolean,
                              createdAt: Timestamp,
                              referNum: Long = 0,
                              updatedAt: Option[Timestamp],
                              srcList: List[AlbumSrc]
                            )




