package joyea_share.action.album

import joyea_share.action.BaseAction
import xitrum.annotation.POST

@POST("/api/v1/album/collect/delete")
class AlbumCollectDeleteAction extends BaseAction[DeleteAlbumCollectReq] {
    override def safeExecute(req: DeleteAlbumCollectReq): Unit = {

    }
}

case class DeleteAlbumCollectReq(
                                  albumId: Long
                                )
