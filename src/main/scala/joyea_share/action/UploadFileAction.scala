package joyea_share.action

import java.io.File
import java.util.UUID

import io.netty.handler.codec.http.multipart.FileUpload
import xitrum.annotation.POST
import xitrum.{Action, SkipCsrfCheck}

@POST("upload")
class UploadFileAction extends Action with SkipCsrfCheck {
  override def execute(): Unit = {
    val uploadFile = param[FileUpload]("data")
    val fileName = uploadFile.getFilename
    val fileSuffix = fileName.substring(fileName.lastIndexOf("."))
    uploadFile.renameTo(new File(s"upload/${UUID.randomUUID().toString}$fileSuffix"))
  }
}
