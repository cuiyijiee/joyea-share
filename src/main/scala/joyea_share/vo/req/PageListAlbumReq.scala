package joyea_share.vo.req

import joyea_share.vo.req
import joyea_share.vo.req.AlbumSortType.AlbumSortType

case class PageListAlbumReq(
                             sortType: AlbumSortType = AlbumSortType.LikeNum,
                             curPage: Int,
                             pageSize: Int,
                             shared: Option[Boolean] = Some(false),
                             hangyeTagId: Int = -1,
                             xianbieTagId: Int = -1,
                             jixingTagId: Int = -1,
                             jieduanTagId: Int = -1,
                             shichangTagId: Int = -1,
                           )


object AlbumSortType extends Enumeration {
  type AlbumSortType = Value

  val LikeNum: req.AlbumSortType.Value = Value(0)
  val CreateAt: req.AlbumSortType.Value = Value(1)
}