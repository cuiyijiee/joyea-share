package joyea_share.action.search

import joyea_share.action.BaseAction
import joyea_share.module.download.DownloadManager
import joyea_share.service.RedisService
import joyea_share.util.LenovoUtil
import joyea_share.vo.lenovo.FtsSearchResp
import org.json4s.jackson.Serialization
import xitrum.annotation.POST

import scala.util.{Failure, Success}

@POST("api/v1/search")
class SearchAction extends BaseAction[SearchRequest] {
    override def safeExecute(req: SearchRequest): Unit = {

        //将关键词写入redis中进行计数
        RedisService.recordSearch(req.searchKey).foreach(result => {
            log.info(s"record search key ${req.searchKey} with result $result")
        })

        LenovoUtil.ftsSearch(
            req.searchKey, "ent", req.offset
        )(DownloadManager.getAdminToken).onComplete {
            case Failure(exception) =>
                exception.printStackTrace()
            case Success(value) =>
                val resp = Serialization.read[FtsSearchResp](value)
                println(s"current find file: ${resp.content.length},and has more: ${resp.has_more},and next_offset is: ${resp.next_offset}")
        }
    }
}

case class SearchRequest(
                          searchKey: String,
                          offset: Int
                        )
