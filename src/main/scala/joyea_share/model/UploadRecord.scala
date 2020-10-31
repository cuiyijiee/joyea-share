package joyea_share.model

import java.time.OffsetDateTime

import scalikejdbc._
import scalikejdbc.async._

import scala.concurrent.Future

case class UploadRecord(
                         id: Long,
                         uploader: String,

                         uploaderName: String = "",

                         srcNeid: Option[Long],
                         srcRev: Option[String],
                         srcType: Option[String],
                         srcHash: Option[String],

                         srcDesc: String,

                         tempSrcName: String,

                         srcName: Option[String],

                         uploadPath: Option[String],
                         uploadPathNeid: Option[Long],
                         createdAt: OffsetDateTime,
                         checked: Boolean,
                         checkedAt: Option[OffsetDateTime],
                         tags: String,
                         refuseReason: Option[String],

                         isPcUpload: Boolean,
                         finished: Boolean
                       )

object UploadRecord extends SQLSyntaxSupport[UploadRecord] with ShortenedNames {

    implicit val session: AsyncDBSession = AsyncDB.sharedSession
    implicit val cxt: EC = ECGlobal

    lazy val ur: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[UploadRecord], UploadRecord] = UploadRecord.syntax("ur")

    override lazy val columns: Seq[String] = autoColumns[UploadRecord]("uploaderName")

    val ju = JoyeaUser.ju

    def apply(sc: SyntaxProvider[UploadRecord])(rs: WrappedResultSet): UploadRecord = apply(sc.resultName)(rs)

    def apply(p: ResultName[UploadRecord])(rs: WrappedResultSet): UploadRecord = autoConstruct(rs, p, "uploaderName")

    def getTagStr(tags: Seq[String]): String = {
        tags.mkString("~~")
    }

    def checkRecord(recordId: Long, allow: Boolean,
                    uploadPath: Option[String], uploadPathNeid: Option[Long], srcName: Option[String],
                    refuseReason: Option[String] = None, checkedAt: OffsetDateTime = OffsetDateTime.now()): Future[Boolean] = withSQL {
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

    def create(uploader: String, srcNeid: Option[Long], srcRev: Option[String], srcType: Option[String],
               srcHash: Option[String], srcDesc: String,
               createdAt: OffsetDateTime = OffsetDateTime.now(),
               tempSrcName: String,
               refuseReason: Option[String] = None, isPcUpload: Boolean = false, finished: Boolean = false,
               checked: Boolean = false, checkedAt: Option[OffsetDateTime] = None, tags: Seq[String]): Future[UploadRecord] = {
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
                    column.refuseReason -> refuseReason,
                    column.isPcUpload -> isPcUpload,
                    column.finished -> finished,
                )
            }.updateAndReturnGeneratedKey().future()
        } yield new UploadRecord(id = id, uploader = uploader, srcHash = srcHash, srcDesc = srcDesc,
            srcRev = srcRev, srcType = srcType, srcNeid = srcNeid, createdAt = createdAt, checked = checked,
            checkedAt = checkedAt, tags = saveTags, refuseReason = refuseReason, tempSrcName = tempSrcName,
            srcName = None, uploadPathNeid = None, uploadPath = None, isPcUpload = isPcUpload, finished = finished)
    }

    def updateStatus(recordId: Long, finished: Boolean = true): Future[Boolean] = {
        withSQL {
            update(UploadRecord).set(
                column.finished -> finished,
            ).where.eq(column.id, recordId)
        }.update().future().map(_ > 0)
    }

    def uploadRecord(recordId: Long, srcNeid: Long, srcRev: String, srcType: String, srcHash: String): Future[Boolean] = {
        withSQL {
            update(UploadRecord).set(
                column.srcNeid -> srcNeid,
                column.srcRev -> srcRev,
                column.srcType -> srcType,
                column.srcHash -> srcHash,
            ).where.eq(column.id, recordId)
        }.update().future().map(_ > 0)
    }

    def findAllByUid(uid: String): Future[List[UploadRecord]] = withSQL {
        selectFrom(UploadRecord as ur)
          .where.eq(column.uploader, uid)
          .and.eq(column.finished, true)
          .orderBy(ur.createdAt).desc
    }.map(UploadRecord(ur)).list().future()

    def pageFind(pageSize: Int = 10, curPage: Int = 1): Future[List[UploadRecord]] = withSQL {
        selectFrom(UploadRecord as ur)
          .where.isNull(column.checkedAt)
          .and.isNotNull(column.srcNeid)
          .and.eq(column.finished, true)
          .orderBy(ur.createdAt).limit(pageSize).offset((curPage - 1) * pageSize)
    }.map(UploadRecord(ur)).list().future()

    def pageFindByUid(uploader: String, pageSize: Int = 10, curPage: Int = 1): Future[List[UploadRecord]] = withSQL {
        selectFrom(UploadRecord as ur).where.isNull(column.checkedAt).and.eq(column.uploader, uploader)
          .and.eq(column.finished, true)
          .orderBy(ur.createdAt).limit(pageSize).offset((curPage - 1) * pageSize)
    }.map(UploadRecord(ur)).list().future()

    def yesterdayUploadRecord(): Future[List[UploadRecord]] = {
        withSQL {
            select.from[UploadRecord](UploadRecord as ur)
              .leftJoin(JoyeaUser as ju).on(ju.joyeaId, ur.uploader)
              .where.isNotNull(ur.checkedAt)
              .and.isNull(ur.refuseReason)
              .and.eq(ur.finished, true)
              .and.append(sqls"""to_days(now()) - to_days(${ur.checkedAt}) = 1""")
              .orderBy(ur.checkedAt).desc  //以审核时间倒叙展示
        }.one(UploadRecord(ur))
          .toOne(JoyeaUser.opt(ju))
          .map((uploadRecord, joyeaUserOpt) => uploadRecord.copy(uploaderName = joyeaUserOpt.map(_.joyeaName).getOrElse("")))
          .list().future()
    }
}