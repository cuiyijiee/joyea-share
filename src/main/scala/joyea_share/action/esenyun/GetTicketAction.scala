package joyea_share.action.esenyun

import joyea_share.action.BaseAction
import joyea_share.util.EsenyunUtil
import xitrum.annotation.POST

@POST("api/v1/esunyun/ticket")
class GetTicketAction extends BaseAction[GetTicketReq] {
  override def safeExecute(req: GetTicketReq): Unit = {
    EsenyunUtil.getTicket().onComplete(safeResponse[String](_, result => {
      baseResponseSuccess(result)
    }))
  }
}

case class GetTicketReq()
