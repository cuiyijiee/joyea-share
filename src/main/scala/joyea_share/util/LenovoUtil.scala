package joyea_share.util

import java.io.IOException

import com.json.JsonObject
import okhttp3._

object LenovoUtil {

  private val BASE_URL = "https://box.lenovo.com/v2"
  //全局搜索的URL
  private val BASE_SEARCH_FTS_URL = "https://console.box.lenovo.com/v2/fts_search"


  /**
    * 登陆接口
    *
    * @param user
    * @param pwd
    * @param listener
    */
  def login(user: String, pwd: String, listener: CommonListener[(String, String)]): Unit = {
    val formBodyBuilder = new FormBody.Builder()
    formBodyBuilder.add("user_slug", s"email:$user")
    formBodyBuilder.add("password", pwd)

    val request = HttpUtil.obtainBaseRequest().url(s"$BASE_URL/user/login").post(formBodyBuilder.build()).build()
    HttpUtil.obtainHttpClient().newCall(request).enqueue(new Callback {
      override def onFailure(call: Call, e: IOException): Unit = {
        listener.onError(s"请求错误:${SUtil.convertExceptionToStr(e)}")
      }

      override def onResponse(call: Call, response: Response): Unit = {
        try {
          val respStr = response.body().string()
          val respJson = JsonObject.readFrom(respStr)
          val session = respJson.getString("X-LENOVO-SESS-ID", null)
          if (session == null) {
            listener.onError(s"登陆错误:$respStr")
          } else {
            listener.onSuccess((session, respJson.getString("user_name", "")))
          }
        } catch {
          case e: Exception =>
            listener.onError(s"请求失败：${SUtil.convertExceptionToStr(e)}")
        }
      }
    })
  }

  /**
    * 注销接口
    *
    * @param user
    * @param listener
    */
  def logout(user: String, listener: CommonListener[String]): Unit = {
    val formBodyBuilder = new FormBody.Builder()
    formBodyBuilder.add("user_slug", s"email:$user")

    val request = HttpUtil.obtainBaseRequest().url(s"$BASE_URL/user/logout").post(formBodyBuilder.build()).build()
    HttpUtil.obtainHttpClient().newCall(request).enqueue(new Callback {
      override def onFailure(call: Call, e: IOException): Unit = {
        listener.onError(s"请求错误:${SUtil.convertExceptionToStr(e)}")
      }

      override def onResponse(call: Call, response: Response): Unit = {
        try {
          val respStr = response.body().string()
          listener.onSuccess(respStr)
        } catch {
          case e: Exception =>
            listener.onError(s"请求失败：${SUtil.convertExceptionToStr(e)}")
        }
      }
    })
  }

  /**
    * 全局搜索接口
    *
    * @param sessionID
    * @param searchKey
    * @param offset
    * @param listener
    */
  def ftsSearch(sessionID: String, searchKey: String, searchType: String = "", offset: Long = 0, listener: CommonListener[JsonObject]): Unit = {

    val urlBuilder = HttpUrl.parse(BASE_SEARCH_FTS_URL).newBuilder()
    urlBuilder.addQueryParameter("with_fts", "true")
      .addQueryParameter("neid", "")
      .addQueryParameter("aid", "")
      .addQueryParameter("path_type", "ent")
      .addQueryParameter("path", "/")
      .addQueryParameter("X-LENOVO-SESS-ID", sessionID)
      .addQueryParameter("query", searchKey)
      .addQueryParameter("type", searchType)
      .addQueryParameter("offset", offset.toString)

    val request = HttpUtil.obtainBaseRequest().url(urlBuilder.build()).build()

    HttpUtil.obtainHttpClient().newCall(request).enqueue(new Callback {
      override def onFailure(call: Call, e: IOException): Unit = {
        listener.onError(s"请求失败：${SUtil.convertExceptionToStr(e)}")
      }

      override def onResponse(call: Call, response: Response): Unit = {
        try {
          listener.onSuccess(JsonObject.readFrom(response.body().string()))
        } catch {
          case e: Exception =>
            listener.onError(s"请求失败：${SUtil.convertExceptionToStr(e)}")
        }
      }
    })
  }

  def obtainFilePreviewUrl(sessionID:String,filePath: String, rev: String, listener: CommonListener[JsonObject]): Unit = {
    val urlBuilder = HttpUrl.parse(s"$BASE_URL/dl_router/databox$filePath").newBuilder()
    urlBuilder
      //.addQueryParameter("root", "/")
      //.addQueryParameter("path", filePath)
      .addQueryParameter("rev", rev)
      .addQueryParameter("path_type", "ent")
      //.addQueryParameter("type", "pic")
      .addQueryParameter("X-LENOVO-SESS-ID", sessionID)

    val request = HttpUtil.obtainBaseRequest().url(urlBuilder.build()).build()

    HttpUtil.obtainHttpClient().newCall(request).enqueue(new Callback {
      override def onFailure(call: Call, e: IOException): Unit = {
        listener.onError(s"请求失败：${SUtil.convertExceptionToStr(e)}")
      }

      override def onResponse(call: Call, response: Response): Unit = {
        try {
          listener.onSuccess(JsonObject.readFrom(response.body().string()))
        } catch {
          case e: Exception =>
            listener.onError(s"请求失败：${SUtil.convertExceptionToStr(e)}")
        }
      }
    })
  }
}
