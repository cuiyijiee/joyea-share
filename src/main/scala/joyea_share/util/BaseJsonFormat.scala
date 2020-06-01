package joyea_share.util

import org.json4s.DefaultFormats

trait BaseJsonFormat {
  implicit val format: DefaultFormats.type = DefaultFormats

}
