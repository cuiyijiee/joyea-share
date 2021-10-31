package joyea_share.action.album

import joyea_share.action.BaseAction
import joyea_share.define.ErrorCode
import joyea_share.model.{Album, AlbumSrc}
import scalikejdbc.async.AsyncDB
import xitrum.annotation.POST

@POST("/api/v1/album/collect/update")
class AlbumCollectionUpdateAction extends BaseAction[UpdateAlbumReq] {
    override def safeExecute(req: UpdateAlbumReq): Unit = {
        Album.findByAlbumId(req.id).onComplete(safeResponse[Option[Album]](_, maybeAlbum => {
            if (maybeAlbum.isDefined) {
                val toEditAlbum = maybeAlbum.get
                AsyncDB.localTx { implicit session => {
                    for {
                        updateResult <- toEditAlbum.copy(albumName = req.name).save()
                        deleteResult <- AlbumSrc.deleteByAlbumId(req.id)
                        createdAlbumSrcNum <- AlbumSrc.createMany(req.src.map(srcReq => {
                            AlbumSrc(albumId = toEditAlbum.albumId,
                              id = 0,
                              srcNeid = srcReq.neid,
                              srcHash = srcReq.hash.getOrElse(""),
                              srcRev = srcReq.rev.getOrElse(""),
                              srcSize = srcReq.size.getOrElse(""),
                              srcPath = srcReq.path,
                              srcType = srcReq.mime_type,
                              srcDesc = srcReq.joyeaDesc,
                              srcFileName = srcReq.filename,
                              srcBytes = srcReq.bytes.getOrElse(0L))
                        }))
                    } yield updateResult
                }
                }.onComplete(safeResponse[Album](_, result => {
                    baseResponseSuccess(true)
                }))
            } else {
                baseResponseError(ErrorCode.albumMenuNotExist)
            }
        }))
    }
}

case class UpdateAlbumReq(
                           id: Long,
                           name: String,
                           src: Seq[AlbumSrcReq]
                         )
