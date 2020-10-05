package joyea_share.module.upload

import java.io.File
import java.util.concurrent.{ExecutorService, Executors}

import com.json.JsonObject
import joyea_share.model.UploadRecord
import joyea_share.module.download.DownloadManager
import joyea_share.util.LenovoUtil
import xitrum.Log

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object UploadManager extends Log {

  implicit val ctx: ExecutionContext = ExecutionContext.Implicits.global
  val executor: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors() + 1)

  def realUpload(recordId: Long): Unit = {
    executor.execute(() => {
      UploadRecord.find(recordId).onComplete {
        case Failure(exception) =>
          log.error("upload exist error: ", exception)
        case Success(maybeRecord) =>
          if (maybeRecord.isDefined) {
            val record = maybeRecord.get
            val toUploadFile = new File(s"upload/${record.tempSrcName}")
            if (toUploadFile.exists()) {
              LenovoUtil.preUpload(DownloadManager.getAdminToken(), toUploadFile).onComplete {
                case Failure(exception) =>
                  log.error("pre upload file exist exception: ", exception)
                case Success(value) =>
                  val json = JsonObject.readFrom(value)
                  val region = json.getString("region", "")
                  LenovoUtil.uploadFile(DownloadManager.getAdminToken(), region, toUploadFile).onComplete {
                    case Failure(exception) =>
                      log.error("real upload file exist exception: ", exception)
                    case Success(value) =>
                      val uploadResultJson = JsonObject.readFrom(value)
                      val srcNeid = uploadResultJson.getLong("neid", 0)
                      val srcHash = uploadResultJson.getString("hash", "")
                      val srcRev = uploadResultJson.getString("rev", "")
                      val srcType = uploadResultJson.getString("mime_type", "")
                      UploadRecord.uploadRecord(recordId, srcNeid, srcRev, srcType, srcHash).onComplete {
                        case Failure(exception) =>
                          log.error("update upload record failed: ", exception)
                        case Success(value) =>
                          log.info(s"update upload record success: ${value}")
                      }
                  }
              }
            } else {
              log.error(s"to upload record: ${recordId} with file ${toUploadFile.getAbsolutePath} not exist!")
            }
          } else {
            log.error(s"to upload record: ${recordId} not found!")
          }
      }
    })
  }
}

case class UploadTask(
                       recordId: String,
                     ) {
}
