package joyea_share.action.album

import joyea_share.action.BaseAction
import joyea_share.model.Album
import xitrum.annotation.POST

@POST("api/v1/album/switch/share")
class SwitchShareAction extends BaseAction[SwitchShareReq] {
  override def safeExecute(req: SwitchShareReq): Unit = {
    Album.switchShare(
      req.albumName,
      req.coverId,
      req.localCoverId,
      req.shareDesc, req.share, req.albumId,
      hangyeTagId = req.hangyeTagId,
      xianbieTagId = req.xianbieTagId,
      jixingTagId = req.jixingTagId,
      jieduanTagId = req.jieduanTagId,
      shichangTagId = req.shichangTagId,
    )
      .onComplete(safeResponse[Boolean](_, result => {
        cyjResponseSuccess(result)
      }))
  }
}

case class SwitchShareReq(
                           coverId: Option[Long],
                           localCoverId: Option[String],
                           shareDesc: Option[String],
                           share: Boolean,
                           albumName: String = "",
                           albumId: Long,
                           hangyeTagId: Int = -1,
                           xianbieTagId: Int = -1,
                           jixingTagId: Int = -1,
                           jieduanTagId: Int = -1,
                           shichangTagId: Int = -1,
                         )