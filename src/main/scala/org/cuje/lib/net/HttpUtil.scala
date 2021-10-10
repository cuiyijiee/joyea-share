package org.cuje.lib.net

import java.io.IOException
import java.net.{InetSocketAddress, Proxy}
import okhttp3._

import java.util
import scala.concurrent.{Future, Promise}

object HttpUtil {

  val okhttpClient: OkHttpClient = new OkHttpClient.Builder()

    .cookieJar(new PersistenceCookieJar)
    .build()

  val JSON: MediaType = MediaType.parse("application/json; charset=utf-8")

  /**
    *
    * @param requestParams 请求参数
    * @return
    */
  def get(
           requestUrl: String,
           requestParams: Map[String, Any],
         ): Future[String] = {
    val promise = Promise[String]

    val urlBuilder = HttpUrl.parse(requestUrl).newBuilder()
    requestParams.foreach(entry => {
      urlBuilder.addQueryParameter(entry._1, entry._2.toString)
    })
    val request = baseRequestBuilder()
      .url(urlBuilder.build())
      .build()
    okhttpClient.newCall(request).enqueue(generateCallback(promise))
    promise.future
  }

  def baseRequestBuilder(): Request.Builder = {
    new Request.Builder()
      .removeHeader("User-Agent")
      .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_0_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.67 Safari/537.36 Edg/87.0.664.47")
  }

  def postForm(
                requestUrl: String,
                postForm: Map[String, Any]
              ): Future[String] = {
    val promise = Promise[String]
    val urlBuilder = HttpUrl.parse(requestUrl).newBuilder().build()
    val requestBody = new FormBody.Builder()
    postForm.foreach(entry => {
      requestBody.add(entry._1, entry._2.toString)
    })
    val requestBuilder = baseRequestBuilder().url(urlBuilder).post(requestBody.build())
    okhttpClient.newCall(requestBuilder.build()).enqueue(generateCallback(promise))
    promise.future
  }

  def postJson(
                requestUrl: String,
                postJsonObj: String,
                headerMap: Map[String, String] = Map()
              ): Future[String] = {
    val promise = Promise[String]
    val urlBuilder = HttpUrl.parse(requestUrl).newBuilder().build()
    val requestBody = RequestBody.create(postJsonObj, JSON)
    val requestBuilder = baseRequestBuilder().url(urlBuilder).post(requestBody)
    headerMap.foreach(entry => {
      requestBuilder.addHeader(entry._1, entry._2)
    })
    okhttpClient.newCall(requestBuilder.build()).enqueue(generateCallback(promise))
    promise.future
  }

  def generateCallback(promise: Promise[String]): Callback = {
    new Callback {
      override def onFailure(call: Call, e: IOException): Unit = {
        promise.failure(e)
      }

      override def onResponse(call: Call, response: Response): Unit = {
        promise.success(response.body().string())
      }
    }
  }
}

class PersistenceCookieJar extends CookieJar {
  val cache = new util.ArrayList[Cookie]()

  import scala.collection.JavaConverters._

  override def loadForRequest(httpUrl: HttpUrl): util.List[Cookie] = {
    //过期的Cookie
    val invalidCookies = new java.util.ArrayList[Cookie]()
    //有效的Cookie
    val validCookies = new java.util.ArrayList[Cookie]()
    cache.asScala.foreach(cookie => {
      if (cookie.expiresAt() < System.currentTimeMillis()) {
        //判断是否过期
        invalidCookies.add(cookie);
      } else if (cookie.matches(httpUrl)) {
        //匹配Cookie对应url
        validCookies.add(cookie);
      }
    })
    //缓存中移除过期的Cookie
    cache.removeAll(invalidCookies);
    //返回List<Cookie>让Request进行设置
    validCookies;
  }

  //Http请求结束，Response中有Cookie时候回调
  override def saveFromResponse(httpUrl: HttpUrl, cookies: util.List[Cookie]): Unit = {
    //内存中缓存Cookie
    cache.addAll(cookies);
  }
}
