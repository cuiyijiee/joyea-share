package joyea_share.action.album

import joyea_share.action.BaseAction
import joyea_share.model.Album
import xitrum.annotation.POST

@POST("api/v1/album/switch/share")
class SwitchShareAction extends BaseAction[SwitchShareReq] {
    override def safeExecute(req: SwitchShareReq): Unit = {
        Album.switchShare(req.coverId, req.shareDesc, req.share, req.albumId)
          .onComplete(safeResponse[Boolean](_, result => {
              cyjResponseSuccess(result)
          }))
    }
}

case class SwitchShareReq(
                           coverId: Option[Long],
                           shareDesc: Option[String],
                           share: Boolean,
                           albumId: Long
                         )