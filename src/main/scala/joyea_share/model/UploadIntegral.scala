package joyea_share.model

import java.time.{LocalDate, OffsetDateTime}

import scalikejdbc._
import scalikejdbc.async._

import scala.concurrent.Future

/**
  * 用户上传积分记录
  *
  * @param id id
  * @param neid  文件id
  * @param uploader 上传者
  * @param integral
  * @param createdAt
  */
case class UploadIntegral(
                           id: Long,
                           neid: Long,
                           uploader: String,
                           integral: Long,
                           createdAt: OffsetDateTime,
                         )


object UploadIntegral extends SQLSyntaxSupport[UploadIntegral] with ShortenedNames {

    implicit val session: AsyncDBSession = AsyncDB.sharedSession
    implicit val cxt: EC = ECGlobal

    lazy val ui: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[UploadIntegral], UploadIntegral] = UploadIntegral.syntax("ui")

    override lazy val columns: Seq[String] = autoColumns[UploadIntegral]()

    val ju: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[JoyeaUser], JoyeaUser] = JoyeaUser.ju

    def apply(sc: SyntaxProvider[UploadIntegral])(rs: WrappedResultSet): UploadIntegral = apply(sc.resultName)(rs)

    def apply(p: ResultName[UploadIntegral])(rs: WrappedResultSet): UploadIntegral = autoConstruct(rs, p)

    def opt(s: SyntaxProvider[UploadIntegral])(rs: WrappedResultSet): Option[UploadIntegral] =
        rs.longOpt(s.resultName.id).map(_ => apply(s.resultName)(rs))

    def create(neid: String, uploader: String, integral: Long, createdAt: OffsetDateTime = OffsetDateTime.now())
              (implicit session: AsyncDBSession = AsyncDB.sharedSession): Future[Long] = {
        withSQL {
            insert.into(UploadIntegral).namedValues(
                column.neid -> neid,
                column.uploader -> uploader,
                column.integral -> integral,
                column.createdAt -> createdAt,
            )
        }.updateAndReturnGeneratedKey().future()
    }

    def findUploadLeaderboardWithDateLimit(startTime: LocalDate, endTime: LocalDate): Future[List[(JoyeaUser, Long)]] = {
        val params = SQLSyntax.ge(ui.createdAt, startTime).and.le(ui.createdAt, endTime)
        findUpload(params)
    }

    def findUpload(queryParams: SQLSyntax)
                  (implicit session: AsyncDBSession = AsyncDB.sharedSession): Future[List[(JoyeaUser, Long)]] = {
        withSQL {
            select.from[JoyeaUser](JoyeaUser as ju)
              .leftJoin(UploadIntegral as ui).on(ju.joyeaId, ui.uploader).append(sqls.and.append(queryParams))
        }.one(JoyeaUser(ju))
          .toMany(UploadIntegral.opt(ui))
          .map((user, integralList) => {
              (user, integralList.foldLeft(0L)((origin, integral) => origin + integral.integral))
          }).list().future()
    }
}