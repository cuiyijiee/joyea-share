package joyea_share.action.album

import java.sql.Timestamp

import joyea_share.action.BaseAction
import joyea_share.db.MySQLSettings
import joyea_share.model.{Album, AlbumSrc}
import joyea_share.vo.req.PageListAlbumReq
import joyea_share.vo.resp.AlbumDetailResp
import scalikejdbc.async.AsyncDB
import xitrum.annotation.POST

import scala.concurrent.Await


@POST("api/v1/album/pageList")
class PageListAction extends BaseAction[PageListAlbumReq] {
  override def safeExecute(req: PageListAlbumReq): Unit = {

    Album.findAllCopied(myUid).onComplete(safeResponse[List[Album]](_, copied => {
      Album.pageListAlbum(req.curPage, req.pageSize, req.shared, req.sortType,
        hangyeTagId = req.hangyeTagId,
        xianbieTagId = req.xianbieTagId,
        jixingTagId = req.jixingTagId,
        jieduanTagId = req.jieduanTagId,
        shichangTagId = req.shichangTagId
      ).onComplete(safeResponse[List[Album]](_, result => {
        cyjResponseSuccess(result.map(album => {
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
            likeNum = album.likeNum,
            updatedAt = album.updatedAt,
            hangyeTagId = album.hangyeTagId,
            xianbieTagId = album.xianbieTagId,
            jixingTagId = album.jixingTagId,
            jieduanTagId = album.jieduanTagId,
            shichangTagId = album.shichangTagId,
            isCopied = copied.exists(_.copyFrom.get == album.albumId),
            srcList = Await.result(AsyncDB.withPool {
              implicit tx => {
                AlbumSrc.findByAlbumId(album.albumId)
              }
            }, MySQLSettings.MYSQL_READ_TIMEOUT),
          )
        }))
      }))
    }))
  }
}




