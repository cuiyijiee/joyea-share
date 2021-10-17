package joyea_share.util

import com.json.JsonObject
import joyea_share.model.NextPlusUser
import org.cuje.lib.net.HttpUtil
import org.json4s.jackson.JsonMethods
import xitrum.Log

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object EsenyunUtil extends Log with  BaseJsonFormat {


  private val CLIENT_ID = "8a9d3ed0603584f501605df3e8860004"
  private val CLIENT_SECRET = "YTliMDEwZTVhZGE3NDA3ZTgxMTQ0OGQ0OTAzYTg3YjE"

  private val URL_TOKEN = "https://open.nextxx.cn/openapi/oauth/token"
  private val URL_TICKET = "https://open.nextxx.cn/openapi/oauth/ticket"
  private val URL_PROFILE = "http://hr.joyea.cn:8099/openapi/find-ytm-user-by-code/"

  def getAccessToken(): Future[String] = {
    val reqJson = new JsonObject()
    reqJson.add("clientId", CLIENT_ID)
    reqJson.add("clientSecret", CLIENT_SECRET)
    HttpUtil.postJson(URL_TOKEN, reqJson.toString(), headerMap = Map()).map(tokenResult => {
      val tokenJson = JsonObject.readFrom(tokenResult)
      val token = tokenJson.getString("access_token", "")
      token
    })
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

  def getUserProfile(authCode: String): Future[NextPlusUser] = {
    HttpUtil.get(s"$URL_PROFILE$authCode", Map()).map(resp => {
      JsonMethods.parse(resp).extract[NextPlusUser]
    })
  }
}

/**
  * {
  * "id" : "NMUAAAXwP5m30gJc",
  * "no" : "崔仪洁",
  * "name" : "崔仪洁",
  * "easUserId" : null,
  * "imgUrl" : null,
  * "phone" : "17756602710",
  * "ytmId" : "2c94919b722bc9f00172fb37fbe100b0",
  * "ytmOpenId" : "aa781395791946bfbb823885cc872d66",
  * "position" : null,
  * "departmentId" : null,
  * "departmentName" : null,
  * "departmentType" : null,
  * "tenantId" : "8a9c8ff360930ca30160959d76390000",
  * "yzjOpenId" : "2c94919b722bc9f00172fb37fbe100b0",
  * "extendInfo" : null
  * }
  */
case class EsenUserProfile(
                            id: String,
                            no: String,
                            name: String,
                            easUserId: String,
                            imgUrl: String,
                            phone: String,
                            ytmId: String,
                            ytmOpenId: String,
                            position: String,
                            departmentId: String,
                            departmentName: String,
                            departmentType: String,
                            tenantId: String,
                            yzjOpenId: String,
                            extendInfo: String
                          )