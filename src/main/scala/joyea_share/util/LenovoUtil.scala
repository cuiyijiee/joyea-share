package joyea_share.util

import java.io.{File, FileOutputStream, IOException, InputStream}

import com.json.JsonObject
import okhttp3._
import org.slf4s.LoggerFactory

object LenovoUtil {

    private val _log = LoggerFactory.getLogger("LenovoUtil")

    private val BASE_URL = "https://box.lenovo.com/v2"
    //全局搜索的URL
    private val BASE_SEARCH_FTS_URL = "https://console.box.lenovo.com/v2/fts_search"
    private val BASE_DOWNLOAD_URL = "https://content.box.lenovo.com/v2/files/databox"

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

    def downloadFile(sessionId: String, rev: String, neid: String, pathType: String = "ent", downloadFilePath: String, listener: CommonListener[File]): Unit = {
        //databox/?X-LENOVO-SESS-ID=e715869294f94d5aa455023fb77132bb_346341_696047_meta&path_type=ent&from=&neid=388924315&rev=9d63422c22be44d9819ea4ef2a3521d5&aid=346341&uid=696047
        val requestUrl = s"$BASE_DOWNLOAD_URL/?X-LENOVO-SESS-ID=$sessionId&path_type=$pathType&neid=$neid&rev=$rev"
        _log.info(s"【download url】: $requestUrl")
        val request = HttpUtil.obtainBaseRequest(sessionId).url(requestUrl).build()
        HttpUtil.obtainHttpClient().newCall(request).enqueue(new Callback {
            override def onFailure(call: Call, e: IOException): Unit = {
                listener.onError("请求失败！")
            }

            override def onResponse(call: Call, response: Response): Unit = {
                var is: InputStream = null
                var os: FileOutputStream = null
                val buf: Array[Byte] = new Array[Byte](2048)
                try {
                    is = response.body().byteStream()
                    val downloadFile = new File(downloadFilePath)
                    os = new FileOutputStream(downloadFile)
                    //val total = response.body().contentLength()
                    var len = is.read(buf)
                    var current = 0
                    while (len != -1) {
                        current = current + len
                        os.write(buf, 0, len)
                        len = is.read(buf)
                    }
                    os.flush()
                    listener.onSuccess(downloadFile)
                } catch {
                    case e: Exception =>
                        listener.onError("请求失败！")
                        _log.error("下载失败：" + SUtil.convertExceptionToStr(e))
                } finally {
                    if (os != null) os.close()
                    if (is != null) is.close()
                }
            }
        })
    }
}
