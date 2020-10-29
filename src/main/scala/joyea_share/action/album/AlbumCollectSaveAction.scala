package joyea_share.action.album

import joyea_share.action.BaseAction
import xitrum.annotation.POST

@POST("/api/v1/album/collect/save")
class AlbumCollectAddAction extends BaseAction[AddAlbumCollectReq] {
    override def safeExecute(req: AddAlbumCollectReq): Unit = {

    }
}

case class AddAlbumCollectReq(
                               albumId: Long
                             )
