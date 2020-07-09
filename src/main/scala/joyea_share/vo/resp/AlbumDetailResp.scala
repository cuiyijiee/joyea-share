package joyea_share.vo.resp

import java.sql.Timestamp

import joyea_share.model.AlbumSrc

case class AlbumDetailResp(
                            albumId: Long,
                            userId: String,
                            userName: String,
                            albumName: String,
                            albumDesc: Option[String],
                            shared: Boolean,
                            shareCoverNeid:Option[Long],
                            shareDesc:Option[String],
                            createdAt: Timestamp,
                            referNum: Long = 0,
                            updatedAt: Option[Timestamp],
                            srcList: List[AlbumSrc]
                          )
