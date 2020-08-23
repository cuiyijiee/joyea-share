package joyea_share.model

import java.sql.Timestamp

import scalikejdbc._
import scalikejdbc.async._

import scala.concurrent.Future

case class UploadRecord(
                         id: Long,
                         uploader: String,

                         srcNeid: Long,
                         srcRev: String,
                         srcType: String,
                         srcHash: String,
                         srcName: String,
                         srcDesc:String,

                         uploadPath: String,
                         uploadPathNeid: Long,
                         createdAt: Timestamp,
                         checked: Boolean,
                         checkedAt: Option[Timestamp],
                         tags: String,
                         refuseReason: Option[String]
                       )

object UploadRecord extends SQLSyntaxSupport[UploadRecord] with ShortenedNames {

  implicit val session: AsyncDBSession = AsyncDB.sharedSession
  implicit val cxt: EC = ECGlobal

  lazy val ur: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[UploadRecord], UploadRecord] = UploadRecord.syntax("ur")

  override def columnNames: Seq[String] = Seq("id", "uploader", "src_name", "src_neid", "src_rev", "src_hash","src_desc", "src_type", "upload_path", "upload_path_neid", "created_at", "checked", "checked_at", "tags", "refuse_reason")

  def apply(sc: SyntaxProvider[UploadRecord])(rs: WrappedResultSet): UploadRecord = apply(sc.resultName)(rs)

  def apply(p: ResultName[UploadRecord])(rs: WrappedResultSet): UploadRecord = autoConstruct(rs, p)

  def getTagStr(tags: Seq[String]): String = {
    tags.mkString("~~")
  }

  def checkRecord(recordId: Long, allow: Boolean, refuseReason: Option[String] = None, checkedAt: Timestamp = new Timestamp(System.currentTimeMillis())): Future[Boolean] = withSQL {
    if (allow) {
      update(UploadRecord).set(
        column.checked -> allow,
        column.checkedAt -> checkedAt
      ).where.eq(column.id, recordId)
    } else {
      update(UploadRecord).set(
        column.checked -> allow,
        column.refuseReason -> refuseReason,
        column.checkedAt -> checkedAt
      ).where.eq(column.id, recordId)
    }
  }.update().future().map(_ > 0)

  def find(id: Long): Future[Option[UploadRecord]] = {
    withSQL {
      selectFrom(UploadRecord as ur).where.eq(ur.id, id)
    }.map(UploadRecord(ur)).single().future()
  }

  def create(uploader: String, srcName: String, srcNeid: Long, srcRev: String, srcType: String, srcHash: String,srcDesc:String,
             uploadPath: String, uploadPathNeid: Long, createdAt: Timestamp = new Timestamp(System.currentTimeMillis()),
             refuseReason: Option[String] = None,
             checked: Boolean = false, checkedAt: Option[Timestamp] = None, tags: Seq[String]): Future[UploadRecord] = {

    val saveTags = getTagStr(tags)

    for {
      id <- withSQL {
        insert.into(UploadRecord).namedValues(
          column.uploader -> uploader,
          column.srcName -> srcName,
          column.srcNeid -> srcNeid,
          column.srcDesc -> srcDesc,
          column.srcRev -> srcRev,
          column.srcType -> srcType,
          column.srcHash -> srcHash,
          column.uploadPath -> uploadPath,
          column.uploadPathNeid -> uploadPathNeid,
          column.createdAt -> createdAt,
          column.checked -> checked,
          column.checkedAt -> checkedAt,
          column.tags -> saveTags,
          column.refuseReason -> refuseReason
        )
      }.updateAndReturnGeneratedKey().future()
    } yield new UploadRecord(id = id, uploader = uploader, uploadPath = uploadPath, uploadPathNeid = uploadPathNeid,
      srcName = srcName, srcHash = srcHash,srcDesc = srcDesc,
      srcRev = srcRev, srcType = srcType, srcNeid = srcNeid, createdAt = createdAt, checked = checked,
      checkedAt = checkedAt, tags = saveTags, refuseReason = refuseReason)
  }

  def findAllByUid(uid: String): Future[List[UploadRecord]] = withSQL {
    selectFrom(UploadRecord as ur).where.eq(column.uploader, uid).orderBy(ur.createdAt)
  }.map(UploadRecord(ur)).list().future()

  def pageFind(pageSize: Int = 10, curPage: Int = 1): Future[List[UploadRecord]] = withSQL {
    selectFrom(UploadRecord as ur).where.isNull(column.checkedAt).orderBy(ur.createdAt).limit(pageSize).offset((curPage - 1) * pageSize)
  }.map(UploadRecord(ur)).list().future()

  def pageFindByUid(uploader: String, pageSize: Int = 10, curPage: Int = 1): Future[List[UploadRecord]] = withSQL {
    selectFrom(UploadRecord as ur).where.isNull(column.checkedAt).orderBy(ur.createdAt).limit(pageSize).offset((curPage - 1) * pageSize)
  }.map(UploadRecord(ur)).list().future()
}