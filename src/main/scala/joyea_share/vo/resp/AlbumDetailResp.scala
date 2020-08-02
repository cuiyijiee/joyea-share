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
                            shareCoverNeid: Option[Long],
                            shareLocalCoverId: Option[String],
                            shareDesc: Option[String],
                            createdAt: Timestamp,
                            referNum: Long = 0,
                            likeNum: Long = 0,
                            hangyeTagId: Int = -1,
                            xianbieTagId: Int = -1,
                            jixingTagId: Int = -1,
                            jieduanTagId: Int = -1,
                            shichangTagId: Int = -1,
                            updatedAt: Option[Timestamp],
                            srcList: List[AlbumSrc]
                          )
