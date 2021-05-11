package joyea_share.util

import java.io.{File, FileNotFoundException, IOException}
import java.net.URLEncoder

import com.json.{JsonArray, JsonObject}
import com.utils.CommonUtil
import okhttp3._
import org.cuje.lib.net.HttpUtil
import xitrum.Log

import scala.concurrent.{Future, Promise}

object LenovoUtil extends Log {

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
  def login(user: String, pwd: String, listener: CommonListener[(String, String, Long)]): Unit = {
    val formBodyBuilder = new FormBody.Builder()
    formBodyBuilder.add("user_slug", s"email:$user")
    formBodyBuilder.add("password", pwd)

    val request = OkHttpUtil.obtainBaseRequest().url(s"$BASE_URL/user/login").post(formBodyBuilder.build()).build()
    OkHttpUtil.okHttpClient.newCall(request).enqueue(new Callback {
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

    val request = OkHttpUtil.obtainBaseRequest().url(s"$BASE_URL/user/logout").post(formBodyBuilder.build()).build()
    OkHttpUtil.okHttpClient.newCall(request).enqueue(new Callback {
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

  def login_(user: String, pwd: String): Future[String] = {
    HttpUtil.postForm(s"$BASE_URL/user/login", Map(
      "user_slug" -> s"email:$user",
      "password" -> pwd
    ))
  }

  def ftsSearch(searchKey: String, searchType: String = "", offset: Int = 0)
               (implicit requestSession: String): Future[String] = {
    HttpUtil.get(
      BASE_SEARCH_FTS_URL,
      Map(
        "with_fts" -> true,
        "neid" -> "",
        "aid" -> "",
        "path_type" -> "ent",
        "path" -> "/",
        "X-LENOVO-SESS-ID" -> requestSession,
        "query" -> searchKey,
        "type" -> searchType,
        "offset" -> offset,
      )
    )
  }

  def getFileStartUrl(neid: Long, rev: String, nsid: Long, mimeType: String, hash: String)(implicit requestSession: String): Future[String] = {
    val accountId = requestSession.split("_")(1)
    val uid = requestSession.split("_")(2)
    HttpUtil.get(
      s"$BASE_URL/preview/router",
      Map(
        "neid" -> neid,
        "nsid" -> nsid,
        "rev" -> rev,
        "X-LENOVO-SESS-ID" -> requestSession,
        "account_id" -> accountId,
        "mime_type" -> mimeType,
        "file_extension" -> mimeType.substring(mimeType.lastIndexOf(".") + 1, mimeType.length),
        "hash" -> hash,
        "type" -> "inner",
        "uid" -> uid
      )
    )
  }

  /**
    * 全局搜索接口
    *
    * @param sessionID
    * @param searchKey
    * @param offset
    * @param listener
    */
  def newFtsSearch(sessionID: String, searchKey: String, searchType: String = "", offset: Long = 0, listener: CommonListener[JsonObject]): Unit = {

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

    val request = OkHttpUtil.obtainBaseRequest().url(urlBuilder.build()).build()

    OkHttpUtil.okHttpClient.newCall(request).enqueue(new Callback {
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

    val request = OkHttpUtil.obtainBaseRequest().url(urlBuilder.build()).build()

    OkHttpUtil.okHttpClient.newCall(request).enqueue(new Callback {
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
    val request = OkHttpUtil.obtainBaseRequest(sessionID).url("https://box.lenovo.com/v2/user/info/get").get().build()
    OkHttpUtil.okHttpClient.newCall(request).enqueue(new Callback {
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
    val request = OkHttpUtil.obtainBaseRequest(sessionId).url(requestUrl).build()
    OkHttpUtil.okHttpClient.newCall(request).enqueue(new Callback {
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

  //https://console.box.lenovo.com/v2/fileops/auth_upload/databox/
  def preUpload(sessionId: String, file: File): Future[String] = {
    val promise = Promise[String]
    val requestUrl = "https://console.box.lenovo.com/v2/fileops/auth_upload/databox/行政自助服务/素材库上传临时文件夹?path_type=ent&bytes=" + file.length()
    val request = OkHttpUtil.obtainBaseRequest(sessionId).url(requestUrl).get().build()
    OkHttpUtil.okHttpClient.newCall(request).enqueue(new Callback {
      override def onFailure(call: Call, e: IOException): Unit = {
        promise.failure(e)
      }

      override def onResponse(call: Call, response: Response): Unit = {
        promise.success(response.body().string())
      }
    })
    promise.future
  }


  def uploadFile(sessionId: String, uploadRegion: String, file: File): Future[String] = {
    val promise = Promise[String]
    if (!file.exists()) {
      promise.failure(new FileNotFoundException(s"to upload file ${file.getAbsolutePath} not exist!"))
    }
    val fileBody = RequestBody.create(file, MediaType.parse("application/octet-stream"))
    val requestBody = new MultipartBody.Builder()
      .setType(MultipartBody.FORM)
      .addFormDataPart("file", file.getName, fileBody)
      .build()

    val uploadUrl = uploadRegion + "/v2/files/databox/行政自助服务/素材库上传临时文件夹/" + file.getName +
      "?path_type=ent&bytes=" + file.length() + "&filename=" + file.getName + "$overwrite=true";

    val request = OkHttpUtil.obtainBaseRequest(sessionId)
      .post(requestBody)
      .url(uploadUrl)
      .build()
    OkHttpUtil.okHttpClient.newCall(request).enqueue(new Callback {
      override def onFailure(call: Call, e: IOException): Unit = {
        promise.failure(e)
      }

      override def onResponse(call: Call, response: Response): Unit = {
        promise.success(response.body().string())
      }
    })
    promise.future
  }

  def downloadFileV2(sessionId: String, path: String, neid: Long, rev: String, pathType: String = "ent", downloadFilePath: String, listener: CommonListener[File]): Unit = {
    val requestUrl = s"${BASE_URL}/dl_router/databox${URLEncoder.encode(path, "UTF-8").replaceAll("\\+", "%20")}?X-LENOVO-SESS-ID=$sessionId&path_type=$pathType&neid=$neid&rev=$rev"
    val request = OkHttpUtil.obtainBaseRequest(sessionId).url(requestUrl).build()
    OkHttpUtil.okHttpClient.newCall(request).enqueue(new Callback {
      override def onFailure(call: Call, e: IOException): Unit = {
        listener.onError("请求失败！")
      }

      override def onResponse(call: Call, response: Response): Unit = {
        try {
          log.info(s"$path size is ${response.body().contentLength()}")
          if (response.code() != 200) {
            log.error(s"请求失败1：$requestUrl,失败代码：${response.code()},失败信息：${response.body().string()}")
            throw new Exception(s"下载失败，服务器返回错误代码:${response.code()}")
          } else {
            val downloadFile = new File(downloadFilePath)
            CommonUtil.writeFile(downloadFile, response.body().byteStream())
            listener.onSuccess(downloadFile)
          }
        } catch {
          case e: Exception =>
            listener.onError("请求失败！")
            log.error("下载失败：" + SUtil.convertExceptionToStr(e))
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
    val request = OkHttpUtil.obtainBaseRequest(sessionID).url(urlBuilder.build()).get().build()
    OkHttpUtil.okHttpClient.newCall(request).enqueue(new Callback {
      override def onFailure(call: Call, e: IOException): Unit = {
        listener.onError("请求失败！")
      }

      override def onResponse(call: Call, response: Response): Unit = {
        val responseJson = JsonObject.readFrom(response.body().string())
        val resultJson = new JsonObject()
        responseJson.get("content").asArray().forEach(value => {
          val json = value.asObject()
          val neidValue = json.get("neid")
          var neid: Long = -1
          if (neidValue.isNumber) {
            neid = neidValue.asLong()
          } else {
            neid = neidValue.asString().toLong
          }
          resultJson.add(neid.toString, json)
        })
        listener.onSuccess(resultJson)
      }
    })
  }

  def renameFile(fileNeid: Long, fileName: String, session: String): Future[Boolean] = {
    val promise = Promise[Boolean]
    val infoJson = new JsonObject()
    infoJson.add("fileName", fileName)
    infoJson.add("op_info", new JsonObject().add("neid", fileNeid))

    val formBodyBuilder = new FormBody.Builder()
    formBodyBuilder.add("op", "rename")
    formBodyBuilder.add("info", infoJson.toString())

    val request = OkHttpUtil.obtainBaseRequest().url(s"$BASE_URL/fileops/manage?X-LENOVO-SESS-ID=${session}").post(formBodyBuilder.build()).build()
    OkHttpUtil.okHttpClient.newCall(request).enqueue(new Callback {
      override def onFailure(call: Call, e: IOException): Unit = {
        promise.failure(e)
      }

      override def onResponse(call: Call, response: Response): Unit = {
        try {
          val respStr = response.body().string()
          log.error(s"rename file resp: $respStr")
          promise.success(true)
        } catch {
          case e: Exception =>
            promise.failure(e)
        }
      }
    })

    promise.future
  }

  def moveFile(toMoveFileNeid: Long, toDirPath: Long, session: String, listener: CommonListener[Boolean]): Unit = {

    val infoJson = new JsonObject()
    infoJson.add("op_info", new JsonArray().add(new JsonObject().add("neid", toMoveFileNeid)))
    infoJson.add("to_info", new JsonObject().add("neid", toDirPath))


    val formBodyBuilder = new FormBody.Builder()
    formBodyBuilder.add("op", "move")
    formBodyBuilder.add("auto_rename", "false")
    formBodyBuilder.add("info", infoJson.toString())

    val request = OkHttpUtil.obtainBaseRequest().url(s"$BASE_URL/fileops/manage?X-LENOVO-SESS-ID=${session}").post(formBodyBuilder.build()).build()
    OkHttpUtil.okHttpClient.newCall(request).enqueue(new Callback {
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
