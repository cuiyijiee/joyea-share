package joyea_share.db

import ch.qos.logback.classic.{Level, Logger}
import org.slf4j.LoggerFactory
import scalikejdbc.async.AsyncConnectionPool
import xitrum.Config

import scala.concurrent._
import duration._

trait MySQLSettings {
    MySQLSettings.initialize()
}

object MySQLSettings {

    val MYSQL_READ_TIMEOUT: FiniteDuration = 30.seconds

    def initialize(): Unit = this.synchronized {
        LoggerFactory.getLogger("scalikejdbc").asInstanceOf[Logger].setLevel(Level.WARN)
        //GlobalSettings.loggingSQLErrors = false

        val mysqlConfig = Config.application.getConfig("mysql")

        // MySQL
        AsyncConnectionPool.singleton(mysqlConfig.getString("url"),
            mysqlConfig.getString("user"),
            mysqlConfig.getString("pwd"))
    }

}
