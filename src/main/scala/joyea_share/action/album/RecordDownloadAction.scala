package joyea_share.action.album

import joyea_share.action.BaseAction
import joyea_share.model.Album
import xitrum.annotation.POST

@POST("api/v1/album/download/record")
class RecordDownloadAction extends BaseAction[RecordDownloadReq] {
    override def safeExecute(req: RecordDownloadReq): Unit = {
        Album.addDownload(req.albumId).onComplete(safeResponse[Boolean](_, result => {
            baseResponseSuccess(result)
        }))
    }
}

case class RecordDownloadReq(albumId: Long)