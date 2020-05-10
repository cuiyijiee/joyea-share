package joyea_share

import java.util.concurrent.ConcurrentHashMap

import joyea_share.db.MySQLSettings
import joyea_share.module.download.DownloadManager
import xitrum.Server

object Boot extends MySQLSettings with DownloadManager {

  private val searchKeyMap: ConcurrentHashMap[String, Int] = new ConcurrentHashMap[String, Int]()

  def main(args: Array[String]): Unit = {
    Server.start()
    Server.stopAtShutdown()
  }

  def addSearchKey(key: String): Unit = {
    searchKeyMap.put(key, if (searchKeyMap.containsKey(key)) {
      searchKeyMap.get(key) + 1
    } else {
      1
    })
  }

  def getLastTenTopKey(): java.util.List[String] = {
    val list = new java.util.ArrayList[java.util.Map.Entry[String, Int]](searchKeyMap.entrySet())
    list.sort((o1: java.util.Map.Entry[String, Int], o2: java.util.Map.Entry[String, Int]) => {
      o2.getValue.compareTo(o1.getValue)
    })
    val size = if (list.size() < 10) list.size() else 10
    val list1 = new java.util.ArrayList[String]()
    for (index <- 0 until size) {
      list1.add(list.get(index).getKey)
    }
    list1
  }
}
