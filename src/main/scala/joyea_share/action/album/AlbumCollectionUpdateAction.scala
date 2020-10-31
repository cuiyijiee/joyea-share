package joyea_share.action.album

import joyea_share.action.BaseAction
import xitrum.annotation.POST

@POST("/api/v1/album/collect/update")
class AlbumCollectionUpdateAction extends BaseAction[UpdateAlbumReq] {
    override def safeExecute(req: UpdateAlbumReq): Unit = {

    }
}

case class UpdateAlbumReq()
