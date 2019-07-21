package joyea_share


import com.json.JsonObject
import joyea_share.util.{CommonListener, LenovoUtil}

object BenchMark {

    def main(args: Array[String]): Unit = {
        val path = "/营销素材展示/005、同行信息"
        val fileName = path.substring(path.lastIndexOf("/") + 1)

        println(fileName)
    }
}
