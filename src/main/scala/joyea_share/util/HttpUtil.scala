package joyea_share.util

import java.util.concurrent.TimeUnit

import okhttp3.{OkHttpClient, Request}

object HttpUtil {

  private var okHttpClient: OkHttpClient = _

  def obtainHttpClient(): OkHttpClient = {

    if (okHttpClient == null) {
      okHttpClient = new OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()
    }
    okHttpClient
  }

  def obtainBaseRequest(): Request.Builder = {
    new Request.Builder()
      .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36")
      .addHeader("Connection", "keep-alive")
  }

}
