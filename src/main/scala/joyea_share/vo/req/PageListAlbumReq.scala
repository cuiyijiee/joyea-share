package joyea_share.vo.req

case class PageListAlbumReq(
                             curPage: Int,
                             pageSize: Int,
                             shared: Option[Boolean] = Some(false)
                           )
