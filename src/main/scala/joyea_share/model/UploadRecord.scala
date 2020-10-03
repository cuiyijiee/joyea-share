package joyea_share.model

import java.time.LocalDateTime

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
                         srcDesc: String,

                         tempSrcName: String,

                         srcName: Option[String],

                         uploadPath: Option[String],
                         uploadPathNeid: Option[Long],
                         createdAt: LocalDateTime,
                         checked: Boolean,
                         checkedAt: Option[LocalDateTime],
                         tags: String,
                         refuseReason: Option[String]
                       )

object UploadRecord extends SQLSyntaxSupport[UploadRecord] with ShortenedNames {

  implicit val session: AsyncDBSession = AsyncDB.sharedSession
  implicit val cxt: EC = ECGlobal

  lazy val ur: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[UploadRecord], UploadRecord] = UploadRecord.syntax("ur")

  override def columnNames: Seq[String] = Seq("id", "uploader", "temp_src_name", "src_name", "src_neid", "src_rev", "src_hash", "src_desc","src_type", "upload_path", "upload_path_neid", "created_at", "checked", "checked_at", "tags", "refuse_reason")

  def apply(sc: SyntaxProvider[UploadRecord])(rs: WrappedResultSet): UploadRecord = apply(sc.resultName)(rs)

  def apply(p: ResultName[UploadRecord])(rs: WrappedResultSet): UploadRecord = autoConstruct(rs, p)

  def getTagStr(tags: Seq[String]): String = {
    tags.mkString("~~")
  }

  def checkRecord(recordId: Long, allow: Boolean,
                  uploadPath: Option[String], uploadPathNeid: Option[Long], srcName: Option[String],
                  refuseReason: Option[String] = None, checkedAt: LocalDateTime = LocalDateTime.now()): Future[Boolean] = withSQL {
    if (allow) {
      update(UploadRecord).set(
        column.checked -> allow,
        column.uploadPath -> uploadPath,
        column.uploadPathNeid -> uploadPathNeid,
        column.srcName -> srcName,
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

  def findByNeid(neid: Long): Future[Option[UploadRecord]] = {
    withSQL {
      selectFrom(UploadRecord as ur).where.eq(ur.srcNeid, neid)
    }.map(UploadRecord(ur)).single().future()
  }

  def find(id: Long): Future[Option[UploadRecord]] = {
    withSQL {
      selectFrom(UploadRecord as ur).where.eq(ur.id, id)
    }.map(UploadRecord(ur)).single().future()
  }

  def create(uploader: String, srcNeid: Long, srcRev: String, srcType: String, srcHash: String, srcDesc: String,
             createdAt: LocalDateTime = LocalDateTime.now(),
             tempSrcName: String,
             refuseReason: Option[String] = None,
             checked: Boolean = false, checkedAt: Option[LocalDateTime] = None, tags: Seq[String]): Future[UploadRecord] = {
    val saveTags = getTagStr(tags)
    for {
      id <- withSQL {
        insert.into(UploadRecord).namedValues(
          column.uploader -> uploader,
          column.srcNeid -> srcNeid,
          column.srcDesc -> srcDesc,
          column.srcRev -> srcRev,
          column.srcType -> srcType,
          column.srcHash -> srcHash,
          column.createdAt -> createdAt,
          column.checked -> checked,
          column.checkedAt -> checkedAt,
          column.tags -> saveTags,
          column.tempSrcName -> tempSrcName,
          column.refuseReason -> refuseReason
        )
      }.updateAndReturnGeneratedKey().future()
    } yield new UploadRecord(id = id, uploader = uploader,  srcHash = srcHash, srcDesc = srcDesc,
      srcRev = srcRev, srcType = srcType, srcNeid = srcNeid, createdAt = createdAt, checked = checked,
      checkedAt = checkedAt, tags = saveTags, refuseReason = refuseReason, tempSrcName = tempSrcName,
      srcName = None, uploadPathNeid = None, uploadPath = None)
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