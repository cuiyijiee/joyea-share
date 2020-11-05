package joyea_share

import joyea_share.db.{MySQLSettings, RedisSetting}
import joyea_share.define.SystemConf
import joyea_share.module.download.DownloadManager
import joyea_share.module.upload.UploadManager
import xitrum.Server

object Boot extends MySQLSettings with RedisSetting with DownloadManager with UploadManager {

    def main(args: Array[String]): Unit = {
        Server.start()
        Server.stopAtShutdown()
    }
}
