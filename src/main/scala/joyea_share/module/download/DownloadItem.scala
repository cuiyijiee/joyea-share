package joyea_share.module.download

import com.json.JsonObject

case class DownloadItem(
                           index: Int,
                           path: String,
                           rev: String,
                           neid: String,
                           fileName: String,
                           pathType: String = "ent",
                       ) {


    def toJson(): JsonObject = {
        new JsonObject()
            .add("path", path)
            .add("rev", rev)
            .add("neid", neid)
            .add("fileName", fileName)
            .add("pathType", pathType)
    }
}
