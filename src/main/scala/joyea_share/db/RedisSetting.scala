package joyea_share.db

import akka.actor.ActorSystem
import redis.{RedisClientPool, RedisServer}
import xitrum.Config

trait RedisSetting {
    RedisSetting.initialize()
}

object RedisSetting {

    private var redisPool: RedisClientPool = _

    def initialize(): Unit = {
        val redisConfig = Config.application.getConfig("redis")
        var redisList: Seq[RedisServer] = Seq()
        for (_ <- 0 until redisConfig.getInt("pool_size")) {
            redisList = redisList :+ RedisServer(
                host = redisConfig.getString("host"),
                port = redisConfig.getInt("port"),
                password = Option(redisConfig.getString("password")),
                db = Option(redisConfig.getInt("db"))
            )
        }
        val actorSystem: ActorSystem = ActorSystem("redis")
        redisPool = RedisClientPool(redisList)(actorSystem)
    }

    def getClientPool: RedisClientPool = {
        this.redisPool
    }
}