package joyea_share.action

import java.io.File
import java.util.UUID

import io.netty.handler.codec.http.multipart.FileUpload
import joyea_share.model.UploadRecord
import joyea_share.module.upload.UploadManager
import xitrum.annotation.POST
import xitrum.{Action, SkipCsrfCheck}

import scala.util.{Failure, Success}

@POST("upload")
class UploadFileAction extends Action with SkipCsrfCheck {
  override def execute(): Unit = {

    val myUid = sessiono[String]("user_id")
    val srcDesc = paramo[String]("srcDesc")

    val uploadFile = param[FileUpload]("file")
    val fileName = uploadFile.getFilename
    val fileSuffix = fileName.substring(fileName.lastIndexOf("."))
    val tempFileName = s"${UUID.randomUUID().toString}$fileSuffix"
    uploadFile.renameTo(new File(s"upload/$tempFileName"))

    UploadRecord.create(uploader = myUid.get, tempSrcName = tempFileName,
      srcNeid = None, srcDesc = srcDesc.getOrElse(""),
      srcType = None, srcRev = None, srcHash = None, tags = Seq(),isPcUpload = true).onComplete {
      case Failure(exception) =>
        log.error("submit upload record success: ", exception)
      case Success(value) =>
        UploadManager.realUpload(value.id)
    }

    respondJson(UploadResult(
      fileName = fileName, tempFileName = tempFileName, fileSize = uploadFile.get().length
    ))
  }
}

case class UploadResult(
                         fileName: String,
                         tempFileName: String,
                         fileSize: Int
                       )