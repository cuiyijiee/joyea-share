package joyea_share

import joyea_share.db.MySQLSettings
import joyea_share.module.download.DownloadManager
import xitrum.Server

object Boot extends MySQLSettings with DownloadManager {

    def main(args: Array[String]): Unit = {
        Server.start()
        Server.stopAtShutdown()
    }
}
