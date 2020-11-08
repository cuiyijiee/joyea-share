package joyea_share.model

import java.time.{LocalDate, OffsetDateTime}

import scalikejdbc._
import scalikejdbc.async._

import scala.concurrent.Future

/**
  * 用户上传文件被引用（加入清单和下载）记录
  *
  * @param id
  * @param neid
  * @param uploader
  * @param quoteBy
  * @param createdAt
  */
case class SrcQuote(
                     id: Long,
                     neid: Long,
                     uploader: String,
                     quoteBy: String,
                     createdAt: OffsetDateTime
                   )

object SrcQuote extends SQLSyntaxSupport[SrcQuote] with ShortenedNames {

    implicit val session: AsyncDBSession = AsyncDB.sharedSession
    implicit val cxt: EC = ECGlobal

    lazy val sq: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[SrcQuote], SrcQuote] = SrcQuote.syntax("sq")

    override lazy val columns: Seq[String] = autoColumns[SrcQuote]()

    val ju: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[JoyeaUser], JoyeaUser] = JoyeaUser.ju

    def apply(sc: SyntaxProvider[SrcQuote])(rs: WrappedResultSet): SrcQuote = apply(sc.resultName)(rs)

    def apply(p: ResultName[SrcQuote])(rs: WrappedResultSet): SrcQuote = autoConstruct(rs, p)

    def opt(s: SyntaxProvider[SrcQuote])(rs: WrappedResultSet): Option[SrcQuote] =
        rs.longOpt(s.resultName.id).map(_ => apply(s.resultName)(rs))

    def create(neid: Long, uploader: String, quoteBy: String, createdAt: OffsetDateTime = OffsetDateTime.now())
              (implicit session: AsyncDBSession = AsyncDB.sharedSession): Future[Long] = {
        withSQL {
            insertInto(SrcQuote)
              .namedValues(
                  column.neid -> neid,
                  column.uploader -> uploader,
                  column.quoteBy -> quoteBy,
                  column.createdAt -> createdAt,
              )
        }.updateAndReturnGeneratedKey().future()
    }

    def findWithDateLimit(startDate: LocalDate, endDate: LocalDate): Future[List[(JoyeaUser, Long)]] = {
        withSQL {
            select.from[JoyeaUser](JoyeaUser as ju)
              .leftJoin(SrcQuote as sq).on(ju.joyeaId, sq.uploader).append(sqls.and.ge(sq.createdAt, startDate).and.le(sq.createdAt, endDate))
        }.one(JoyeaUser(ju))
          .toMany(SrcQuote.opt(sq))
          .map((user, record) => {
              (user, record.length.toLong)
          }).list().future()
    }
}



