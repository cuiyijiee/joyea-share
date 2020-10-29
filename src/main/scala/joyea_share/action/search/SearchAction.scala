package joyea_share.action.search

import joyea_share.action.BaseAction
import joyea_share.service.RedisService
import xitrum.annotation.POST

@POST("api/v1/search")
class SearchAction extends BaseAction[SearchRequest] {
  override def safeExecute(req: SearchRequest): Unit = {

    //将关键词写入redis中进行计数
    RedisService.recordSearch(req.searchKey).foreach(result => {
      log.info(s"record search key ${req.searchKey} with result $result")
    })


  }
}

case class SearchRequest(
                          searchKey: String,
                          offset: Int
                        )
