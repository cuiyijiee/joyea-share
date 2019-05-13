package joyea_share.db

import scalikejdbc.async.AsyncConnectionPool
import xitrum.Config

import scala.concurrent._
import duration._

trait MySQLSettings {
  MySQLSettings.initialize()
}

object MySQLSettings {

  private var isInitialized = false

  val MYSQL_READ_TIMEOUT: FiniteDuration = 5.seconds

  def initialize(): Unit = this.synchronized {
    if (isInitialized) return
    //GlobalSettings.loggingSQLErrors = false

    val mysqlConfig = Config.application.getConfig("server").getConfig("mysql")

    // MySQL
    AsyncConnectionPool.singleton(mysqlConfig.getString("url"),
      mysqlConfig.getString("user"),
      mysqlConfig.getString("pwd"))
    isInitialized = true
  }

}
