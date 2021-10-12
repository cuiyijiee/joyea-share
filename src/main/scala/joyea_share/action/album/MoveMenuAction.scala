package joyea_share.action.album

import joyea_share.action.BaseAction
import joyea_share.define.ErrorCode
import joyea_share.model.{Album, AlbumMenu}
import xitrum.annotation.POST

@POST("api/v1/album/moveMenu")
class MoveMenuAction extends BaseAction[MoveMenuReq] {
    override def safeExecute(req: MoveMenuReq): Unit = {
        if (req.menuId == -1) {
            Album.moveMenu(req.albumId, None)
              .onComplete(safeResponse[Boolean](_, baseResponseSuccess))
        } else {
            AlbumMenu.find(req.menuId)
              .onComplete(safeResponse[Option[AlbumMenu]](_, maybeMenu => {
                  if (maybeMenu.isDefined) {
                      Album.moveMenu(req.albumId, Option(req.menuId))
                        .onComplete(safeResponse[Boolean](_, baseResponseSuccess))
                  } else {
                      baseResponseError(ErrorCode.albumMenuNotExist)
                  }
              }))
        }
    }
}

case class MoveMenuReq(
                        albumId: Long,
                        menuId: Long
                      )
