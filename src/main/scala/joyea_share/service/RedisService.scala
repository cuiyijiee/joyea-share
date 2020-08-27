package joyea_share.service

import joyea_share.db.RedisSetting
import redis.RedisClientPool
import xitrum.Log

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object RedisService extends RedisSetting with Log {

  val redis: RedisClientPool = RedisSetting.getClientPool

  val SearchRankKey: String = "search_rank"
  val FileRefRankKey: String = "file_ref_rank"
  val FileDownloadRankKey: String = "file_download_rank"
  val AlbumRefRankKey: String = "album_download_rank"
  val UploadRankKey:String = "upload_rank"

  def recordUpload(uploadJoyeaId:String):Future[Double] = {
    redis.zincrby(UploadRankKey, 1, uploadJoyeaId)
  }

  def recordSearch(searchKey: String): Future[Double] = {
    redis.zincrby(SearchRankKey, 1, searchKey)
  }

  def getSearchTopList(top: Int): Future[Seq[(String, Double)]] = {
    redis.zrevrangeWithscores[String](SearchRankKey, 0, top - 1)
  }

  def recordFileRef(neid: String): Future[Long] = {
    redis.hincrby(FileRefRankKey, neid ,1)
  }

  def findFileRefNum(neid: String): Future[Long] = {
    redis.hget[Double](FileRefRankKey, neid).map(_.getOrElse[Double](0).toLong)
  }

  def recordFileDownload(neid: String): Future[Long] = {
    redis.hincrby(FileDownloadRankKey, neid ,1)
  }

  def findFileDownloadNum(neid: String): Future[Long] = {
    redis.hget[Double](FileDownloadRankKey, neid).map(_.getOrElse[Double](0).toLong)
  }
}
