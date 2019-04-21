package joyea_share

import com.json.{JsonObject, WriterConfig}
import joyea_share.util.{CommonListener, LenovoUtil}

object BenchMark {

  def main(args: Array[String]): Unit = {

    LenovoUtil.login("phdycn@joyea.cn", "1234567890@joyea", new CommonListener[(String,String)] {
      override def onSuccess(obj: (String,String)): Unit = {
        LenovoUtil.obtainFilePreviewUrl(obj._1,"/一号园IT项目组/003访客安防门禁/园区门禁安防-海康技术问题确认.png","7a825cb62e2348b39ab02fea0e6a2556",new CommonListener[JsonObject] {
          override def onSuccess(obj: JsonObject): Unit = {
            println(obj)
          }

          override def onError(error: String): Unit = {

          }
        })
      }

      override def onError(error: String): Unit = {
        println(error)
      }
    })
  }
}
