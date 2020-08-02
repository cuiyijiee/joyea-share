package joyea_share.util

import joyea_share.vo.req.AlbumSortType
import org.json4s.ext.EnumSerializer
import org.json4s.{DefaultFormats, Formats}


trait BaseJsonFormat {
  implicit val formats: AnyRef with Formats = DefaultFormats + new EnumSerializer(AlbumSortType)

}