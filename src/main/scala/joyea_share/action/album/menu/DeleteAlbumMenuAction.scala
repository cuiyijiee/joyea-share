package joyea_share.action.album.menu

import joyea_share.action.BaseAction
import joyea_share.define.ErrorCode
import joyea_share.model.{Album, AlbumMenu}
import xitrum.annotation.POST

@POST("api/v1/album/deleteMenu")
class DeleteAlbumMenuAction extends BaseAction[DeleteAlbumMenuReq] {
    override def safeExecute(req: DeleteAlbumMenuReq): Unit = {
        Album.countByMenu(req.menuId)
          .onComplete(safeResponse[Long](_, count => {
              if (count > 0) {
                  baseResponseError(ErrorCode.albumMenuHasAlbumExist)
              } else {
                  AlbumMenu.delete(req.menuId).onComplete(safeResponse[Int](_, num => {
                      baseResponseSuccess(num > 0)
                  }))
              }
          }))
    }
}


case class DeleteAlbumMenuReq(
                               menuId: Long
                             )