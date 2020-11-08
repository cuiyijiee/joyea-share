package joyea_share.action.album

import joyea_share.action.BaseAction
import joyea_share.define.ErrorCode
import joyea_share.model.{Album, AlbumSrc}
import scalikejdbc.async.AsyncDB
import xitrum.annotation.POST

@POST("/api/v1/album/collect/save")
class AlbumCollectAddAction extends BaseAction[SaveAlbumCollectReq] {
  override def safeExecute(req: SaveAlbumCollectReq): Unit = {
    //查询是否重名
    Album.findByUserIdAndName(myUid, req.name)
      .onComplete(safeResponse[Option[Album]](_, result => {
        if (result.isDefined) {
          baseResponseError(ErrorCode.albumHasExist)
        } else {
          //如果没有创建过则使用事务创建该记录
          AsyncDB.localTx { implicit session => {
            for {
              createdAlbum <- Album.create(myUid, userName = myName, albumName = req.name, albumDesc = None)
              createdAlbumSrcNum <- AlbumSrc.createMany(req.src.map(srcReq => {
                AlbumSrc(albumId = createdAlbum.albumId, id = 0, srcNeid = srcReq.neid, srcHash = srcReq.hash, srcRev = srcReq.rev, srcSize = srcReq.size, srcPath = srcReq.path, srcType = srcReq.mime_type, srcDesc = srcReq.joyeaDesc, srcFileName = srcReq.filename, srcBytes = srcReq.bytes)
              }))
            } yield createdAlbum
          }
          }.onComplete(safeResponse[Album](_, createdAlbum => {
            baseResponseSuccess(createdAlbum.albumId)
          }))
        }
      }))
  }
}

case class SaveAlbumCollectReq(
                                name: String,
                                src: Seq[AlbumSrcReq]
                              )


/*
srcNeid = src.getLong("neid", -1L),
                        srcPath = src.getString("path", ""),
                        srcSize = src.getString("size", ""),
                        srcHash = src.getString("hash", ""),
                        srcRev = src.getString("rev", ""),
                        srcDesc = src.getString("joyeaDesc", ""),
                        srcFileName = src.getString("filename", ""),
                        srcBytes = src.getLong("bytes", 0),
                        srcType = src.getString("mime_type", ""),
 */

case class AlbumSrcReq(
                        neid: Long,
                        path: String,
                        size: String,
                        hash: String,
                        rev: String,
                        joyeaDesc: String,
                        filename: String,
                        bytes: Long,
                        mime_type: String
                      )