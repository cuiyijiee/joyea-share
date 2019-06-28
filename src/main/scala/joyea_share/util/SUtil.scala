package joyea_share.util

import java.text.SimpleDateFormat
import java.util.Date

/**
  * Created by ivy on 2019-04-03
  */
object SUtil {


    def convertExceptionToStr(throwable: Throwable): String = {
        val sb = new StringBuffer()
        sb.append(throwable.getLocalizedMessage).append("\n")
        throwable.getStackTrace.foreach(stack => {
            sb.append("\t").append(stack.toString).append("\n")
        })
        sb.toString
    }

    def genDateString(date: java.util.Date = new java.util.Date(), formatString: String = "yy-MM-dd HH:mm:ss"): String = {
        new SimpleDateFormat(formatString).format(date)
    }

    import java.util.Calendar

    /**
      * 获取下一个小时的指定分钟
      *
      * @param nextMinute
      * @return
      */
    def getNextMinuteHour(nextMinute: Int): Date = {
        val c = Calendar.getInstance
        val currentMinute = c.get(Calendar.MINUTE)
        // 时间超过指定分钟，设置为小一个小时的指定分钟
        if (currentMinute >= nextMinute) {
            val currentHour = c.get(Calendar.HOUR)
            if (currentHour == 23) {
                c.add(Calendar.DATE, 1)
                c.set(Calendar.HOUR, 0)
            } else c.add(Calendar.HOUR, 1)
        }
        c.set(Calendar.MINUTE, nextMinute)
        c.set(Calendar.SECOND, 0)
        c.getTime
    }
}
