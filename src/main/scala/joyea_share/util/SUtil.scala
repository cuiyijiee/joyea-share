package joyea_share.util

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

}
