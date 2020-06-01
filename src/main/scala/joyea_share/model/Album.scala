package joyea_share.model

import java.sql.Timestamp

import scalikejdbc._
import async._
import com.json.JsonObject
import joyea_share.util.SUtil

import scala.concurrent.Future

case class Album(
                    albumId: Long,
                    userId: String,
                    userName: String,
                    albumName: String,
                    albumDesc: Option[String],
                    shared: Boolean,
                    createdAt: Timestamp,
                    referNum: Long = 0,
                    updatedAt: Option[Timestamp]
                ) extends ShortenedNames {

    def save(): Future[Album] = Album.save(this)

    def toJson: JsonObject = new JsonObject()
        .add("album_id", this.albumId)
        .add("user_id", this.userId)
        .add("user_name", this.userName)
        .add("album_name", this.albumName)
        .add("album_desc", this.albumDesc.getOrElse(""))
        .add("shared", this.shared)
        .add("refer_num", this.referNum)
        .add("created_at", SUtil.genDateString(this.createdAt, "yy-MM-dd HH:mm"))
        .add("updated_at", SUtil.genDateString(this.updatedAt.getOrElse(this.createdAt), "yy-MM-dd HH:mm"))
}


object Album extends SQLSyntaxSupport[Album] with ShortenedNames {

    lazy val a: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[Album], Album] = Album.syntax("a")

    override def columnNames: Seq[String] = Seq("album_id", "user_id", "user_name", "album_name", "album_desc", "shared", "created_at", "refer_num", "updated_at")

    def apply(a: SyntaxProvider[Album])(rs: WrappedResultSet): Album = apply(a.resultName)(rs)

    def apply(a: ResultName[Album])(rs: WrappedResultSet): Album = {
        new Album(
            albumId = rs.get[Long](a.albumId),
            userId = rs.get[String](a.userId),
            userName = rs.get[String](a.userName),
            albumName = rs.get[String](a.albumName),
            albumDesc = rs.get[Option[String]](a.albumDesc),
            shared = rs.get[Boolean](a.shared),
            createdAt = rs.get[Timestamp](a.createdAt),
            referNum = rs.get[Long](a.referNum),
            updatedAt = rs.get[Option[Timestamp]](a.updatedAt)
        )
    }

    def save(album: Album, updateAt: Option[Timestamp] = Some(new Timestamp(System.currentTimeMillis())))
            (implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Album] = withSQL {
        update(Album).set(
            column.userId -> album.userId,
            column.userName -> album.userName,
            column.albumName -> album.albumName,
            column.albumDesc -> album.albumDesc,
            column.shared -> album.shared,
            column.referNum -> album.referNum,
            column.updatedAt -> updateAt
        ).where.eq(column.albumId, album.albumId)
    }.update().future().map(_ => album)

    //查找该用户的所有清单
    def findByUserId(userId: String)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[List[Album]] = withSQL {
        selectFrom(Album as a).where.eq(column.userId, userId).orderBy(column.albumId).desc
    }.map(Album(a)).list().future()

    //查找是否存在同名的
    def findByUserIdAndName(userId: String, albumName: String)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Option[Album]] = withSQL {
        selectFrom(Album as a).where.eq(column.userId, userId).and.eq(column.albumName, albumName)
    }.map(Album(a)).single().future()

    def findByAlbumId(albumId: Long)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Option[Album]] = withSQL {
        selectFrom(Album as a).where.eq(column.albumId, albumId)
    }.map(Album(a)).single().future()

    def create(userId: String, userName: String, albumName: String, albumDesc: Option[String], shared: Boolean = false, referNum: Long = 0, createdAt: Timestamp = new Timestamp(System.currentTimeMillis()), updatedAt: Option[Timestamp] = None)
              (implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Album] = {
        for {
            albumId <- withSQL {
                insertInto(Album).namedValues(
                    column.userId -> userId,
                    column.userName -> userName,
                    column.albumName -> albumName,
                    column.albumDesc -> albumDesc,
                    column.shared -> shared,
                    column.referNum -> referNum,
                    column.createdAt -> createdAt,
                    column.updatedAt -> updatedAt,
                )
            }.updateAndReturnGeneratedKey().future()
        } yield new Album(albumId = albumId, userId = userId, userName = userName, albumName = albumName, albumDesc = albumDesc, shared = shared, createdAt = createdAt, updatedAt = updatedAt)
    }

    def delete(albumId: Long)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Int] = withSQL {
        deleteFrom(Album).where.eq(column.albumId, albumId)
    }.update().future()

    def shareAlbum(albumId: Long, updatedAt: Option[Timestamp] = Option(new Timestamp(System.currentTimeMillis())))(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Int] = withSQL {
        update(Album).set(
            column.shared -> true,
            column.updatedAt -> updatedAt
        ).where.eq(column.albumId, albumId)
    }.update().future()

    def unShared(albumId: Long, updatedAt: Option[Timestamp] = Option(new Timestamp(System.currentTimeMillis())))(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Int] = withSQL {
        update(Album).set(
            column.shared -> false,
            column.updatedAt -> updatedAt
        ).where.eq(column.albumId, albumId)
    }.update().future()

    def addRefer(albumId: Long)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Int] = this.synchronized {
        withSQL {
            update(Album).set(sqls"""${Album.column.referNum} = ${Album.column.referNum} + 1""").where.eq(column.albumId, albumId)
        }.update().future()
    }

    def removeRefer(albumId: Long)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Int] = withSQL {
        update(Album).set(sqls"""${Album.column.referNum} = ${Album.column.referNum} - 1""").where.eq(column.albumId, albumId)
    }.update().future()

    def searchByName(keyword: String)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[List[Album]] = withSQL {
        selectFrom(Album as a).where.like(column.albumName, s"%$keyword%")
    }.map(Album(a)).list().future()

}
