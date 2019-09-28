package joyea_share


import java.io.File

import com.json.JsonObject
import joyea_share.util.{CommonListener, LenovoUtil, ZipUtils}

object BenchMark {

    def main(args: Array[String]): Unit = {
        ZipUtils.compressZip(Array("/Users/cuje/test/123","/Users/cuje/test/234"),"/Users/cuje/test/test.zip")
    }
}
