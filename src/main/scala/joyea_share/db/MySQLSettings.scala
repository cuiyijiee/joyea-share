package joyea_share.db

import scalikejdbc.async.AsyncConnectionPool
import xitrum.Config

import scala.concurrent._
import scala.concurrent.duration._

trait MySQLSettings {
    MySQLSettings.initialize()
}

object MySQLSettings {

    val MYSQL_READ_TIMEOUT: FiniteDuration = 30.seconds

    def initialize(): Unit = this.synchronized {
        val mysqlConfig = Config.application.getConfig("mysql")
        // MySQL
        AsyncConnectionPool.singleton(mysqlConfig.getString("url"),
            mysqlConfig.getString("user"),
            mysqlConfig.getString("pwd"))
    }

}
