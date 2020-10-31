package org.cuje.lib

import java.time.{Instant, OffsetDateTime, ZoneId}

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
}
