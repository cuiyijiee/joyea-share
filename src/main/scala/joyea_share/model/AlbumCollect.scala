package joyea_share.model

import java.sql.Timestamp

import scalikejdbc._
import scalikejdbc.async._

import scala.concurrent.Future

case class AlbumCollect(
                         id: Long,
                         userId: String,
                         albumId: Long,
                         createdAt: Timestamp
                       )

object AlbumCollect extends SQLSyntaxSupport[AlbumCollect] with ShortenedNames {

    lazy val ac: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[AlbumCollect], AlbumCollect]
    = AlbumCollect.syntax("ac")

    override def columnNames: Seq[String] = Seq("id", "user_id", "album_id", "created_at")

    def apply(ac: SyntaxProvider[AlbumCollect])(rs: WrappedResultSet): AlbumCollect = apply(ac.resultName)(rs)

    def apply(a: ResultName[AlbumCollect])(rs: WrappedResultSet): AlbumCollect = autoConstruct(rs, a)

    def create(userId: String, albumId: Long, createdAt: Timestamp = new Timestamp(System.currentTimeMillis()))
              (implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[AlbumCollect] = {
        for {
            collectId <- withSQL {
                insertInto(AlbumCollect).namedValues(
                    column.userId -> userId,
                    column.albumId -> albumId,
                )
            }.updateAndReturnGeneratedKey().future()
        } yield new AlbumCollect(id = collectId, userId = userId, albumId = albumId, createdAt = createdAt)
    }

    def delete(collectId: Long)
              (implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal)
    : Future[Boolean] = withSQL {
        deleteFrom(AlbumCollect).where.eq(ac.id, collectId)
    }.update().future().map(_ >= 1)


}