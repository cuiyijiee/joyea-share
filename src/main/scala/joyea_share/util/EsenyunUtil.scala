package joyea_share.util

import com.json.{JsonObject, WriterConfig}
import joyea_share.model.NextPlusUser
import org.cuje.lib.net.HttpUtil
import org.json4s.jackson.JsonMethods
import xitrum.Log

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object EsenyunUtil extends Log with BaseJsonFormat {


  private val CLIENT_ID = "8a9d3ed0603584f501605df3e8860004"
  private val CLIENT_SECRET = "YTliMDEwZTVhZGE3NDA3ZTgxMTQ0OGQ0OTAzYTg3YjE"

  private val URL_TOKEN = "https://open.nextxx.cn/openapi/oauth/token"
  private val URL_TICKET = "https://open.nextxx.cn/openapi/oauth/ticket"
  private val URL_PROFILE = "http://hr.joyea.cn:8099/openapi/find-ytm-user-by-code/"
  private val WORD_LIST = "https://open.nextxx.cn/wordsapi/words/list/my-words-all"

  private var accessToken = "";
  private var nextAccessTokenRefreshTime: Long = 0;

  /**
    * 根据返回的过期时间缓存一份accessToken，不用每次都用服务器获取
    *
    * @return
    */
  def getAccessToken(): Future[String] = {
    if (accessToken.isEmpty || System.currentTimeMillis() > nextAccessTokenRefreshTime) {
      val reqJson = new JsonObject()
      reqJson.add("clientId", CLIENT_ID)
      reqJson.add("clientSecret", CLIENT_SECRET)
      HttpUtil.postJson(URL_TOKEN, reqJson.toString(), headerMap = Map()).map(tokenResult => {
        val tokenJson = JsonObject.readFrom(tokenResult)
        val token = tokenJson.getString("access_token", "")
        accessToken = token
        val expiresIn = tokenJson.getInt("expires_in", 0)
        nextAccessTokenRefreshTime = System.currentTimeMillis() + expiresIn * 1000
        token
      })
    } else {
      Future(accessToken)
    }
  }

  def getTicket(): Future[String] = {
    val tokenResult = Await.result(getAccessToken(), Duration.Inf)
    val reqJson = new JsonObject()
    reqJson.add("accessToken", tokenResult)
    HttpUtil.postJson(URL_TICKET, reqJson.toString(), headerMap = Map("Authorization" -> s"Bearer $tokenResult")).map(result => {
      val tokenJson = JsonObject.readFrom(result)
      val ticket = tokenJson.getString("ticket", "")
      ticket
    })
  }

  def getYtmWordList(tenantId: String, userId: String, searchText: Option[String]): Future[EsenWordResp] = {
    val tokenResult = Await.result(getAccessToken(), Duration.Inf)
    val reqJson = new JsonObject()
    reqJson.add("accessToken", tokenResult)
    reqJson.add("tenantId", tenantId)
    reqJson.add("operatorId", userId)
    reqJson.add("userId", userId)
    //    reqJson.add("inIds", tokenResult)
    if (searchText.isEmpty || searchText.get.isEmpty) {

    } else {
      reqJson.add("text", searchText.orNull)
    }
    reqJson.add("queryType", "PARTICIPATE")
    reqJson.add("sort", "CREATEDTIMEDESC")
    HttpUtil.postJson(WORD_LIST, reqJson.toString(), headerMap = Map("Authorization" -> s"Bearer $tokenResult")).map(result => {
      log.info("获取小白板请求参数：" + reqJson.toString(WriterConfig.PRETTY_PRINT) + "\n服务器返回:" + JsonObject.readFrom(result).toString(WriterConfig.PRETTY_PRINT))
      JsonMethods.parse(result).extract[EsenWordResp]
    })
  }

  def getUserProfile(authCode: String): Future[NextPlusUser] = {
    HttpUtil.get(s"$URL_PROFILE$authCode", Map()).map(resp => {
      JsonMethods.parse(resp).extract[NextPlusUser]
    })
  }
}

///**
//  * {
//  * "id" : "NMUAAAXwP5m30gJc",
//  * "no" : "崔仪洁",
//  * "name" : "崔仪洁",
//  * "easUserId" : null,
//  * "imgUrl" : null,
//  * "phone" : "17756602710",
//  * "ytmId" : "2c94919b722bc9f00172fb37fbe100b0",
//  * "ytmOpenId" : "aa781395791946bfbb823885cc872d66",
//  * "position" : null,
//  * "departmentId" : null,
//  * "departmentName" : null,
//  * "departmentType" : null,
//  * "tenantId" : "8a9c8ff360930ca30160959d76390000",
//  * "yzjOpenId" : "2c94919b722bc9f00172fb37fbe100b0",
//  * "extendInfo" : null
//  * }
//  */
//case class EsenUserProfile(
//                            id: String,
//                            no: String,
//                            name: String,
//                            easUserId: String,
//                            imgUrl: String,
//                            phone: String,
//                            ytmId: String,
//                            ytmOpenId: String,
//                            position: String,
//                            departmentId: String,
//                            departmentName: String,
//                            departmentType: String,
//                            tenantId: String,
//                            yzjOpenId: String,
//                            extendInfo: String
//                          )

case class EsenWord(
                     id: String,
                     title: String,
                     content: String
                   )

case class EsenWordResp(
                         total_number: Int,
                         words: List[EsenWord],
                         stickyWords: String,
                         sticky_total_number: Int
                       )