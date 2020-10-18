package joyea_share.module.download

import java.time.LocalDateTime

import scalikejdbc._
import scalikejdbc.async._

import scala.concurrent.Future

case class DownloadRecord(
                           index: Int,
                           filePath: String,
                           joyeaUserId: String,
                           rev: String,
                           neid: Long,
                           fileName: String,
                           pathType: String = "ent",
                           createdAt: LocalDateTime = LocalDateTime.now()
                         ) {
  def recordToDb(): Future[Long] = {
    DownloadRecord.create(this.joyeaUserId, this.filePath, this.neid, this.fileName, createdAt)
  }
}

object DownloadRecord extends SQLSyntaxSupport[DownloadRecord] with ShortenedNames {

  implicit val session: AsyncDBSession = AsyncDB.sharedSession
  implicit val cxt: EC = ECGlobal

  lazy val dr: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[DownloadRecord], DownloadRecord] = DownloadRecord.syntax("dr")

  override lazy val columns: Seq[String] = autoColumns[DownloadRecord]("index", "rev", "pathType")

  def apply(sc: SyntaxProvider[DownloadRecord])(rs: WrappedResultSet): DownloadRecord = apply(sc.resultName)(rs)

  def apply(p: ResultName[DownloadRecord])(rs: WrappedResultSet): DownloadRecord = autoConstruct(rs, p)

  def create(joyeaUserId: String, filePath: String, neid: Long, fileName: String, createdAt: LocalDateTime = LocalDateTime.now()): Future[Long] = {
    withSQL {
      insertInto(DownloadRecord).namedValues(
        column.joyeaUserId -> joyeaUserId,
        column.filePath -> filePath,
        column.neid -> neid,
        column.fileName -> fileName,
        column.createdAt -> createdAt,
      )
    }.updateAndReturnGeneratedKey.future()
  }
}