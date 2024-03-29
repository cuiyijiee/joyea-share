package joyea_share.action.album

import joyea_share.action.BaseAction
import joyea_share.define.ErrorCode
import joyea_share.model.{Album, AlbumSrc, SrcQuote, UploadRecord}
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
                          createdAlbumId <- Album.create(myUid, userName = myName, albumName = req.name, albumDesc = None)
                          createdAlbumSrcNum <- AlbumSrc.createMany(req.src.map(srcReq => {
                              AlbumSrc(albumId = createdAlbumId, id = 0,
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
                      } yield createdAlbumId
                  }
                  }.onComplete(safeResponse[Long](_, createdAlbumId => {
                      baseResponseSuccess(createdAlbumId)
                      req.src.foreach(src => {
                          UploadRecord.findByNeid(src.neid)
                            .foreach(recordOpt => {
                                if (recordOpt.isDefined) {
                                    SrcQuote.create(src.neid, recordOpt.get.uploader, myUid)
                                } else {
                                    log.info(s"record ${src.neid} is not upload by server, no need to record src quote!")
                                }
                            })
                      })
                  }))
              }
          }))
    }
}

case class SaveAlbumCollectReq(
                                name: String,
                                src: Seq[AlbumSrcReq]
                              )

case class AlbumSrcReq(
                        neid: String,
                        path: String,
                        size: Option[String],
                        hash: Option[String],
                        rev: Option[String],
                        joyeaDesc: String,
                        filename: String,
                        bytes: Option[Long],
                        mime_type: String
                      )
