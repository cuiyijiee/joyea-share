package org.cuje.lib.net

import java.io.IOException
import java.net.{InetSocketAddress, Proxy}

import okhttp3._

import scala.concurrent.{Future, Promise}

object HttpUtil {

  val okhttpClient: OkHttpClient = new OkHttpClient.Builder()
//    .proxy(new Proxy(Proxy.Type.HTTP,new InetSocketAddress("10.80.1.18", 7890)))
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
    val request = new Request.Builder().url(urlBuilder.build()).build()
    okhttpClient.newCall(request).enqueue(generateCallback(promise))
    promise.future
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
    val requestBuilder = new Request.Builder().url(urlBuilder).post(requestBody.build())
    okhttpClient.newCall(requestBuilder.build()).enqueue(generateCallback(promise))
    promise.future
  }

  def postJson(
                requestUrl: String,
                postJsonObj: String
              ): Future[String] = {
    val promise = Promise[String]
    val urlBuilder = HttpUrl.parse(requestUrl).newBuilder().build()
    val requestBody = RequestBody.create(postJsonObj, JSON)
    val requestBuilder = new Request.Builder().url(urlBuilder).post(requestBody)
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
