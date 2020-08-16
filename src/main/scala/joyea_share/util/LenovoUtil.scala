package joyea_share.util

import java.io.{BufferedInputStream, File, FileOutputStream, IOException, InputStream}
import java.net.URLEncoder
import java.util.concurrent.{ExecutorService, Executors}

import cats.effect.{Blocker, ContextShift, IO}
import com.json.{JsonArray, JsonObject}
import com.utils.CommonUtil
import okhttp3._
import org.http4s.client.{Client, JavaNetClientBuilder}
import org.slf4s.LoggerFactory
import xitrum.Log

import scala.concurrent.ExecutionContext

object LenovoUtil extends Log {

  implicit val cs: ContextShift[IO] = IO.contextShift(ExecutionContext.global)
  val blockingPool: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors() + 1)
  val blocker: Blocker = Blocker.liftExecutorService(blockingPool)
  val httpClient: Client[IO] = JavaNetClientBuilder[IO](blocker).create


  private val BASE_URL = "https://box.lenovo.com/v2"
  //全局搜索的URL
  private val BASE_SEARCH_FTS_URL = "https://console.box.lenovo.com/v2/fts_search"
  private val DOWNLOAD_TIMEOUT = 2 * 60 * 1000

  /**
    * 登陆接口
    *
    * @param user
    * @param pwd
    * @param listener
    */
  def login(user: String, pwd: String, listener: CommonListener[(String, String, Long)]): Unit = {
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
            listener.onSuccess((session, respJson.getString("user_name", ""), respJson.getLong("user_id", -1)))
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

  def obtainFilePreviewUrl(sessionID: String, filePath: String, rev: String, listener: CommonListener[JsonObject]): Unit = {
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

  def getUserInfo(sessionID: String, listener: CommonListener[JsonObject]): Unit = {
    val request = HttpUtil.obtainBaseRequest(sessionID).url("https://box.lenovo.com/v2/user/info/get").get().build()
    HttpUtil.obtainHttpClient().newCall(request).enqueue(new Callback {
      override def onFailure(call: Call, e: IOException): Unit = {
        listener.onError("请求失败！")
      }

      override def onResponse(call: Call, response: Response): Unit = {
        val resultJson = JsonObject.readFrom(response.body().string())
        if (resultJson.getInt("state", -1) == 401) {
          listener.onError(resultJson.get("message").asString())
        } else {
          listener.onSuccess(resultJson)
        }
      }
    })
  }

  def listDir(sessionId: String, path: String, pathType: String = "ent", listener: CommonListener[JsonObject]): Unit = {
    val requestUrl = s"$BASE_URL/metadata/root${URLEncoder.encode(path, "UTF-8").replaceAll("\\+", "%20")}?X-LENOVO-SESS-ID=$sessionId&path_type=$pathType"
    val request = HttpUtil.obtainBaseRequest(sessionId).url(requestUrl).build()
    HttpUtil.obtainHttpClient().newCall(request).enqueue(new Callback {
      override def onFailure(call: Call, e: IOException): Unit = {
        listener.onError("请求失败！")
      }

      override def onResponse(call: Call, response: Response): Unit = {
        val resultJson = JsonObject.readFrom(response.body().string())
        if (resultJson.getInt("state", -1) == 401) {
          listener.onError(resultJson.get("message").asString())
        } else {
          listener.onSuccess(resultJson)
        }
      }
    })
  }

  def downloadFileV2(sessionId: String, path: String, neid: String, rev: String, pathType: String = "ent", downloadFilePath: String, listener: CommonListener[File]): Unit = {
    val requestUrl = s"${BASE_URL}/dl_router/databox${URLEncoder.encode(path, "UTF-8").replaceAll("\\+", "%20")}?X-LENOVO-SESS-ID=$sessionId&path_type=$pathType&neid=$neid&rev=$rev"
    val request = HttpUtil.obtainBaseRequest(sessionId).url(requestUrl).build()
    HttpUtil.obtainHttpClient().newCall(request).enqueue(new Callback {
      override def onFailure(call: Call, e: IOException): Unit = {
        listener.onError("请求失败！")
      }

      override def onResponse(call: Call, response: Response): Unit = {
        log.info(s"$path size is ${response.body().contentLength()}")
        if (response.code() != 200) {
          log.error(s"请求失败：$requestUrl,失败代码：${response.code()},失败信息：${response.body().string()}")
          listener.onError(s"请求失败：$requestUrl,失败代码：${response.code()},失败信息：${response.body().string()}")
        } else {
          try {
            val downloadFile = new File(downloadFilePath)
            CommonUtil.writeFile(downloadFile, response.body().byteStream())
            listener.onSuccess(downloadFile)
          } catch {
            case e: Exception =>
              listener.onError("请求失败！")
              log.error("下载失败：" + SUtil.convertExceptionToStr(e))
          }
        }
      }
    })
  }

  //{
  //    "content": [
  //        {
  //            "comment_num": 0,
  //            "bookmark_id": 0,
  //            "neid": 1084283049,
  //            "tags": []
  //        }
  //    ]
  //}
  def getExtraMeta(sessionID: String, neids: java.util.List[Long], listener: CommonListener[JsonObject]): Unit = {

    val requestNeidsSb = new StringBuffer("[")
    neids.forEach(neid => {
      requestNeidsSb.append(neid.toString).append(",")
    })
    requestNeidsSb.append("]")

    val urlBuilder = HttpUrl.parse("https://console.box.lenovo.com/v2/extra_meta").newBuilder()
    urlBuilder.addQueryParameter("neids", requestNeidsSb.toString)
    val request = HttpUtil.obtainBaseRequest(sessionID).url(urlBuilder.build()).get().build()
    HttpUtil.obtainHttpClient().newCall(request).enqueue(new Callback {
      override def onFailure(call: Call, e: IOException): Unit = {
        listener.onError("请求失败！")
      }

      override def onResponse(call: Call, response: Response): Unit = {
        val responseJson = JsonObject.readFrom(response.body().string())
        val resultJson = new JsonObject()
        responseJson.get("content").asArray().forEach(value => {
          val json = value.asObject()
          resultJson.add(json.get("neid").asLong().toString, json)
        })
        listener.onSuccess(resultJson)
      }
    })
  }

  def moveFile(toMoveFileNeid: Long, toDirPath: Long, session: String, listener: CommonListener[Boolean]): Unit = {

    val infoJson = new JsonObject()
    infoJson.add("op_info", new JsonArray().add(new JsonObject().add("neid", toMoveFileNeid)))
    infoJson.add("to_info", new JsonObject().add("neid", toDirPath))


    val formBodyBuilder = new FormBody.Builder()
    formBodyBuilder.add("op", "move")
    formBodyBuilder.add("auto_rename", "false")
    formBodyBuilder.add("info", infoJson.toString())

    val request = HttpUtil.obtainBaseRequest().url(s"$BASE_URL/fileops/manage?X-LENOVO-SESS-ID=${session}").post(formBodyBuilder.build()).build()
    HttpUtil.obtainHttpClient().newCall(request).enqueue(new Callback {
      override def onFailure(call: Call, e: IOException): Unit = {
        listener.onError(s"请求错误:${SUtil.convertExceptionToStr(e)}")
      }

      override def onResponse(call: Call, response: Response): Unit = {
        try {
          val respStr = response.body().string()
          log.error(s"move file resp: $respStr")
          listener.onSuccess(true)
        } catch {
          case e: Exception =>
            listener.onError(s"请求失败：${SUtil.convertExceptionToStr(e)}")
        }
      }
    })
  }
}
