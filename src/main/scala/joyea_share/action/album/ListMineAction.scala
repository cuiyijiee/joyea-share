package joyea_share.action.album

import joyea_share.action.BaseAction
import joyea_share.db.MySQLSettings
import joyea_share.model.{Album, AlbumSrc}
import joyea_share.vo.req.EmptyReq
import joyea_share.vo.resp.AlbumDetailResp
import scalikejdbc.async.AsyncDB
import xitrum.annotation.POST

import scala.concurrent.Await

@POST("api/v1/album/listMine")
class ListMineAction extends BaseAction[ListMyMenuAlbumReq] {
    override def safeExecute(req: ListMyMenuAlbumReq): Unit = {
        Album.findByUserAndMenu(myUid, req.menuId)
          .onComplete(safeResponse[Seq[Album]](_, albumList => {
              baseResponseSuccess(albumList.map(album => {
                  AlbumDetailResp(
                      albumId = album.albumId,
                      userId = album.userId,
                      userName = album.userName,
                      albumName = album.albumName,
                      albumDesc = album.albumDesc,
                      shared = album.shared,
                      shareCoverNeid = album.shareCoverNeid,
                      shareLocalCoverId = album.shareLocalCoverId,
                      shareDesc = album.shareDesc,
                      createdAt = album.createdAt,
                      referNum = album.referNum,
                      updatedAt = album.updatedAt,
                      srcList = album.src.toList,
                      menu = album.menu
                  )
              }))
          }))
    }
}

case class ListMyMenuAlbumReq(
                               menuId: Long = -1L
                             )


