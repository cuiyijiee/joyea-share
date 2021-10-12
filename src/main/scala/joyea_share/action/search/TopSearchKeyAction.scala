package joyea_share.action.search

import joyea_share.action.BaseAction
import joyea_share.service.RedisService
import joyea_share.vo.req.EmptyReq
import xitrum.annotation.POST

@POST("api/v1/search/key/top")
class TopSearchKeyAction extends BaseAction[EmptyReq] {
  override def safeExecute(req: EmptyReq): Unit = {
    RedisService.getSearchTopList(10).onComplete(safeResponse[Seq[(String, Double)]](_, resultList => {
      baseResponseSuccess(resultList.map(_._1))
    }))
  }
}
