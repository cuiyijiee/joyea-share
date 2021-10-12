package joyea_share.action.upload

import joyea_share.action.BaseAction
import joyea_share.model.{JoyeaUser, UploadRecord}
import xitrum.annotation.POST

import scala.concurrent.Future

@POST("api/v1/upload/get")
class PageGetUploadRecord extends BaseAction[PageGetUploadRecordReq] {
  override def safeExecute(req: PageGetUploadRecordReq): Unit = {
    UploadRecord.pageFind(pageSize = req.pageSize, curPage = req.curPage)
      .onComplete(safeResponse[List[UploadRecord]](_, result => {
        Future.sequence(result.map(_.uploader).distinct.map(joyeaId => {
          JoyeaUser.findByJoyeaId(joyeaId)
        })).onComplete(safeResponse[List[Option[JoyeaUser]]](_, userList => {
          baseResponseSuccess(
            UploadRecordResp(
              record = result, user = userList.filter(_.isDefined).map(_.get).map(user => {
                user.joyeaId -> user.joyeaName
              }).toMap
            )
          )
        }))
      }))
  }
}

case class PageGetUploadRecordReq(
                                   pageSize: Int = 10,
                                   curPage: Int = 1,
                                 )

case class UploadRecordResp(
                             record: List[UploadRecord],
                             user: Map[String, String]
                           )