package joyea_share.model

import java.time.{LocalDate, OffsetDateTime}

import scalikejdbc._
import scalikejdbc.async._

import scala.concurrent.Future

case class AlbumQuote(
                       id: Long,
                       albumId: Long,
                       creator: String,
                       quoteBy: String,
                       createdAt: OffsetDateTime
                     )


object AlbumQuote extends SQLSyntaxSupport[AlbumQuote] with ShortenedNames {

    implicit val session: AsyncDBSession = AsyncDB.sharedSession
    implicit val cxt: EC = ECGlobal

    lazy val aq: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[AlbumQuote], AlbumQuote] = AlbumQuote.syntax("aq")

    override lazy val columns: collection.Seq[String] = autoColumns[AlbumQuote]()

    val ju: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[JoyeaUser], JoyeaUser] = JoyeaUser.ju

    def apply(sc: SyntaxProvider[AlbumQuote])(rs: WrappedResultSet): AlbumQuote = apply(sc.resultName)(rs)

    def apply(p: ResultName[AlbumQuote])(rs: WrappedResultSet): AlbumQuote = autoConstruct(rs, p)

    def opt(s: SyntaxProvider[AlbumQuote])(rs: WrappedResultSet): Option[AlbumQuote] =
        rs.longOpt(s.resultName.id).map(_ => apply(s.resultName)(rs))

    def create(albumId: Long, creator: String, quoteBy: String, createdAt: OffsetDateTime = OffsetDateTime.now())
              (implicit session: AsyncDBSession = AsyncDB.sharedSession): Future[Long] = {
        withSQL {
            insertInto(AlbumQuote)
              .namedValues(
                  column.albumId -> albumId,
                  column.creator -> creator,
                  column.quoteBy -> quoteBy,
                  column.createdAt -> createdAt,
              )
        }.updateAndReturnGeneratedKey().future()
    }

    def findWithDateLimit(startDate: LocalDate, endDate: LocalDate): Future[List[(JoyeaUser, Long)]] = {
        withSQL {
            select.from[JoyeaUser](JoyeaUser as ju)
              .leftJoin(AlbumQuote as aq).on(ju.joyeaId, aq.creator).append(sqls.and.ge(aq.createdAt, startDate).and.le(aq.createdAt, endDate))
        }.one(JoyeaUser(ju))
          .toMany(AlbumQuote.opt(aq))
          .map((user, record) => {
              (user, record.length.toLong)
          }).list().future()
    }
}
