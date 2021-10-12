package joyea_share.action.album.menu

import joyea_share.action.BaseAction
import joyea_share.define.ErrorCode
import joyea_share.model.AlbumMenu
import xitrum.annotation.POST

@POST("api/v1/album/createMenu")
class CreateAlbumMenuAction extends BaseAction[CreateAlbumReq] {
    override def safeExecute(req: CreateAlbumReq): Unit = {
        AlbumMenu.findByUserIdAndMenuName(myUid, req.menuName)
          .onComplete(safeResponse[Option[AlbumMenu]](_, maybeMenu => {
              if (maybeMenu.isDefined) {
                  baseResponseError(ErrorCode.albumMenuHasExist)
              } else {
                  AlbumMenu.create(myUid, req.menuName)
                    .onComplete(safeResponse[Long](_, menuId => {
                        baseResponseSuccess(menuId)
                    }))
              }
          }))
    }
}


case class CreateAlbumReq(
                           menuName: String
                         )