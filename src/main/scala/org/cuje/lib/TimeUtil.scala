package org.cuje.lib

import java.text.SimpleDateFormat
import java.time.{Instant, OffsetDateTime, ZoneId}
import java.util.Date

object TimeUtil {
  /**
    * 时间戳转换成时间类
    *
    * @param timestamp
    * @return
    */
  def timestampToOffsetDataTime(timestamp: Long): OffsetDateTime = {
    Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toOffsetDateTime
  }

  def offsetDataTimeToTimestamp(dateTime: OffsetDateTime): Long = {
    dateTime.toInstant.toEpochMilli
  }

  def getCurrentDateStr(format: String = "yyyy-MM-dd HH:mm:sss"): String = {
    val date = new Date()
    new SimpleDateFormat(format).format(date)
  }
}
