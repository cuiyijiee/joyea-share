package joyea_share.action.album.menu

import joyea_share.action.BaseAction
import joyea_share.define.ErrorCode
import joyea_share.model.AlbumMenu
import xitrum.annotation.POST

@POST("api/v1/album/renameMenu")
class RenameAlbumMenuAction extends BaseAction[RenameMenuReq] {
    override def safeExecute(req: RenameMenuReq): Unit = {
        AlbumMenu.findByUserIdAndMenuName(myUid, req.name)
          .onComplete(safeResponse[Option[AlbumMenu]](_, maybeMenu => {
              if (maybeMenu.isDefined) {
                  baseResponseError(ErrorCode.albumMenuHasExist)
              } else {
                  AlbumMenu.renameMenu(req.menuId, req.name)
                    .onComplete(safeResponse[Int](_, result => {
                        baseResponseSuccess(result > 0)
                    }))
              }
          }))
    }
}

case class RenameMenuReq(
                          menuId: Long,
                          name: String
                        )
