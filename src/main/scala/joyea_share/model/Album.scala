package joyea_share.model

import java.sql.Timestamp

import scalikejdbc._
import async._
import com.json.JsonObject

import scala.concurrent.Future

case class Album(
                  albumId: Long,
                  userId: Long,
                  albumName: String,
                  albumDesc: Option[String],
                  createdAt: Timestamp,
                  updatedAt: Option[Timestamp]
                ) extends ShortenedNames {
  def toJson: JsonObject = new JsonObject()
    .add("album_id", this.albumId)
    .add("user_id", this.userId)
    .add("album_name", this.albumName)
    .add("album_desc", this.albumDesc.getOrElse(""))
    .add("created_at", this.createdAt.getTime)
    .add("updated_at", if (updatedAt.isDefined) updatedAt.get.getTime else 0)
}


object Album extends SQLSyntaxSupport[Album] with ShortenedNames {

  lazy val a: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[Album], Album] = Album.syntax("a")

  override def columnNames: Seq[String] = Seq("album_id", "user_id", "album_name", "album_desc", "created_at", "updated_at")

  def apply(a: SyntaxProvider[Album])(rs: WrappedResultSet): Album = apply(a.resultName)(rs)

  def apply(a: ResultName[Album])(rs: WrappedResultSet): Album = {
    new Album(
      albumId = rs.get[Long](a.albumId),
      userId = rs.get[Long](a.userId),
      albumName = rs.get[String](a.albumName),
      albumDesc = rs.get[Option[String]](a.albumDesc),
      createdAt = rs.get[Timestamp](a.createdAt),
      updatedAt = rs.get[Option[Timestamp]](a.updatedAt)
    )
  }

  //查找该用户的所有清单
  def findByUserId(userId: Long)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[List[Album]] = withSQL {
    selectFrom(Album as a).where.eq(column.userId, userId)
  }.map(Album(a)).list().future()

  //查找是否存在同名的
  def findByUserIdAndName(userId: Long, albumName: String)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Option[Album]] = withSQL {
    selectFrom(Album as a).where.eq(column.userId, userId).and.eq(column.albumName, albumName)
  }.map(Album(a)).single().future()

  def create(userId: Long, albumName: String, albumDesc: Option[String], createdAt: Timestamp = new Timestamp(System.currentTimeMillis()), updatedAt: Option[Timestamp] = None)
            (implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Album] = {
    for {
      albumId <- withSQL {
        insertInto(Album).namedValues(
          column.userId -> userId,
          column.albumName -> albumName,
          column.albumDesc -> albumDesc,
          column.createdAt -> createdAt,
          column.updatedAt -> updatedAt,
        )
      }.updateAndReturnGeneratedKey().future()
    } yield new Album(albumId = albumId, userId = userId, albumName = albumName, albumDesc = albumDesc, createdAt = createdAt, updatedAt = updatedAt)
  }

  def delete(albumId: Long)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Int] = withSQL {
    deleteFrom(Album).where.eq(column.albumId, albumId)
  }.update().future()

}
