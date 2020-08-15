package joyea_share.action.album

import joyea_share.action.BaseAction
import joyea_share.model.{Album, AlbumSrc}
import xitrum.annotation.POST

@POST("api/v1/album/copy")
class CopyAlbum extends BaseAction[CopyAlbumReq] {
  override def safeExecute(req: CopyAlbumReq): Unit = {
    Album.findByAlbumId(req.albumId).onComplete(safeResponse[Option[Album]](_, albumOpt => {
      if (albumOpt.isDefined) {
        val album = albumOpt.get
        Album.create(myUid, myName, album.albumName, albumDesc = album.albumDesc, shared = false, referNum = 0, downloadNum = 0, likeNum = 0,
          hangyeTagId = album.hangyeTagId, xianbieTagId = album.xianbieTagId, jixingTagId = album.jixingTagId, jieduanTagId = album.jieduanTagId, shichangTagId = album.shichangTagId,
          copyFrom = Some(album.albumId)
        )
          .onComplete(safeResponse[Album](_, newAlbum => {
            AlbumSrc.findByAlbumId(album.albumId).onComplete(safeResponse[List[AlbumSrc]](_, srcList => {
              srcList.foreach(src => {
                AlbumSrc.create(src.srcNeid, newAlbum.albumId, src.srcPath, src.srcType, src.srcHash, src.srcRev, src.srcSize, src.srcDesc, src.srcFileName, srcBytes = src.srcBytes)
              })
            }))
            Album.addLike(albumId = album.albumId).onComplete(safeResponse[Int](_, result => {
              cyjResponseSuccess(true)
            }))
          }))
      }
    }))
  }
}

case class CopyAlbumReq(
                         albumId: Long
                       )
