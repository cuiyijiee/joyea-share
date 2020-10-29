package org.cuje.lib.net

import java.io.IOException

import okhttp3._

import scala.concurrent.{Future, Promise}

object HttpUtil {

  val okhttpClient: OkHttpClient = new OkHttpClient.Builder()
    .build()

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
    okhttpClient.newCall(request).enqueue(new Callback {
      override def onFailure(call: Call, e: IOException): Unit = {
        promise.failure(e)
      }

      override def onResponse(call: Call, response: Response): Unit = {
        promise.success(response.body().string())
      }
    })
    promise.future
  }


}
