package joyea_share

import joyea_share.db.MySQLSettings
import xitrum.Server

object Boot extends MySQLSettings {

  def main(args: Array[String]): Unit = {
    Server.start()
    Server.stopAtShutdown()
  }
}
