package joyea_share.util

import java.text.SimpleDateFormat

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

}
