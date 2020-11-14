package joyea_share.action.album.menu

import joyea_share.action.BaseAction
import joyea_share.model.AlbumMenu
import joyea_share.vo.req.EmptyReq
import xitrum.annotation.POST

@POST("api/v1/album/menuList")
class ListMyAlbumMenu extends BaseAction[EmptyReq] {
    override def safeExecute(req: EmptyReq): Unit = {
        AlbumMenu.findByUserId(myUid).onComplete(safeResponse(_, baseResponseSuccess))
    }
}
