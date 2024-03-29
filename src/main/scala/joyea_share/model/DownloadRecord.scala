package joyea_share.model

import java.time.{LocalDate, OffsetDateTime}

import scalikejdbc._
import scalikejdbc.async._

import scala.concurrent.Future

case class DownloadRecord(
                           id: Int = 0,
                           index: Int,
                           filePath: String,
                           joyeaUserId: String,
                           rev: String,
                           neid: String,
                           fileName: String,
                           pathType: String = "ent",
                           createdAt: OffsetDateTime = OffsetDateTime.now()
                         ) {
    def recordToDb(): Future[Long] = {
        DownloadRecord.create(this.joyeaUserId, this.filePath, this.neid, this.fileName, createdAt)
    }
}

object DownloadRecord extends SQLSyntaxSupport[DownloadRecord] with ShortenedNames {

    implicit val session: AsyncDBSession = AsyncDB.sharedSession
    implicit val cxt: EC = ECGlobal

    lazy val dr: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[DownloadRecord], DownloadRecord] = DownloadRecord.syntax("dr")

    lazy val ju: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[JoyeaUser], JoyeaUser] = JoyeaUser.ju

    override lazy val columns: collection.Seq[String] = autoColumns[DownloadRecord]("index", "rev", "pathType")

    def apply(sc: SyntaxProvider[DownloadRecord])(rs: WrappedResultSet): DownloadRecord = apply(sc.resultName)(rs)

    def apply(p: ResultName[DownloadRecord])(rs: WrappedResultSet): DownloadRecord = autoConstruct(rs, p)

    def opt(s: SyntaxProvider[DownloadRecord])(rs: WrappedResultSet): Option[DownloadRecord] =
        rs.longOpt(s.resultName.id).map(_ => apply(s.resultName)(rs))

    def create(joyeaUserId: String, filePath: String, neid: String, fileName: String, createdAt: OffsetDateTime = OffsetDateTime.now())
              (implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Long] = {
        withSQL {
            insertInto(DownloadRecord).namedValues(
                column.joyeaUserId -> joyeaUserId,
                column.filePath -> filePath,
                column.neid -> neid,
                column.fileName -> fileName,
                column.createdAt -> createdAt,
            )
        }.updateAndReturnGeneratedKey().future()
    }

    def getTodayDownload(joyeaUserId: String)
                        (implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Seq[DownloadRecord]] = {
        withSQL {
            selectFrom(DownloadRecord as dr)
              .where
              .eq(dr.joyeaUserId, joyeaUserId)
              .and.append(sqls"""TO_DAYS( NOW( ) ) - TO_DAYS( ${dr.createdAt}) <= 1""")
        }.map(DownloadRecord(dr)).list().future()
    }

    def find(taskId: Long)
            (implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Option[DownloadRecord]] = {
        withSQL {
            selectFrom(DownloadRecord as dr)
              .where.eq(dr.id, taskId)
        }.map(DownloadRecord(dr)).single().future()
    }

//    def findWithDateLimit(startDate: LocalDate, endDate: LocalDate): Future[List[(JoyeaUser, Long)]] = {
//        withSQL {
//            select.from[JoyeaUser](JoyeaUser as ju)
//              .leftJoin(DownloadRecord as dr).on(sqls.and.ge(dr.createdAt, startDate).and.le(dr.createdAt, endDate))
//        }.one(JoyeaUser(ju))
//          .toMany(DownloadRecord.opt(dr))
//          .map((user, record) => {
//              (user, record.length.toLong)
//          }).list().future()
//    }

    //    def finish(taskId: Long)
    //              (implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Boolean] = {
    //        withSQL {
    //            update(DownloadRecord).set(
    //                dr.finishedAt -> sqls.currentTimestamp
    //            ).where.eq(dr.id, taskId)
    //        }.update().future().map(_ > 0)
    //    }
}