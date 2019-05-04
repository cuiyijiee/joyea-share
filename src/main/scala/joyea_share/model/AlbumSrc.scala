package joyea_share.model

import java.sql.Timestamp

import scalikejdbc._
import async._
import com.json.JsonObject
import scala.concurrent.Future

case class AlbumSrc(
                       id: Long,
                       albumId: Long,
                       srcNeid: Long,
                       srcHash: String,
                       srcRev: String,
                       srcSize: String,
                       srcPath: String,
                       srcType: String,
                       srcDesc: String,
                       createdAt: Timestamp
                   ) extends ShortenedNames {

    def toJson: JsonObject = new JsonObject()
        .add("id", this.id)
        .add("neid", this.srcNeid)
        .add("album_id", this.albumId)
        .add("path", this.srcPath)
        .add("type", this.srcType)
        .add("rev", this.srcRev)
        .add("size", this.srcSize)
        .add("cp ", this.srcDesc)
        .add("created_at", this.createdAt.getTime)

}

object AlbumSrc extends SQLSyntaxSupport[AlbumSrc] with ShortenedNames {

    lazy val albums: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[AlbumSrc], AlbumSrc] = AlbumSrc.syntax("albums")

    override def columnNames: Seq[String] = Seq("id", "album_id", "src_neid", "src_path", "src_type", "src_hash", "src_rev", "src_size", "src_desc", "created_at")

    def apply(as: SyntaxProvider[AlbumSrc])(rs: WrappedResultSet): AlbumSrc = apply(as.resultName)(rs)

    def apply(as: ResultName[AlbumSrc])(rs: WrappedResultSet): AlbumSrc = {
        new AlbumSrc(
            id = rs.get[Long](as.id),
            albumId = rs.get[Long](as.albumId),
            srcNeid = rs.get[Long](as.srcNeid),
            srcPath = rs.get[String](as.srcPath),
            srcType = rs.get[String](as.srcType),
            srcHash = rs.get[String](as.srcHash),
            srcRev = rs.get[String](as.srcRev),
            srcSize = rs.get[String](as.srcSize),
            srcDesc = rs.get[String](as.srcDesc),
            createdAt = rs.get[Timestamp](as.createdAt)
        )
    }

    def create(srcNeid: Long, albumId: Long, srcPath: String, srcType: String, srcHash: String, srcRev: String,
               srcSize: String, srcDesc: String, createdAt: Timestamp = new Timestamp(System.currentTimeMillis()))
              (implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[AlbumSrc] = {
        for {
            id <- withSQL {
                insertInto(AlbumSrc).namedValues(
                    column.srcNeid -> srcNeid,
                    column.albumId -> albumId,
                    column.srcPath -> srcPath,
                    column.srcType -> srcType,
                    column.srcHash -> srcHash,
                    column.srcRev -> srcRev,
                    column.srcSize -> srcSize,
                    column.srcDesc -> srcDesc,
                    column.createdAt -> createdAt
                )
            }.updateAndReturnGeneratedKey().future()
        } yield AlbumSrc(id = id, srcNeid = srcNeid, albumId = albumId, srcPath = srcPath, srcType = srcType,
            srcHash = srcHash, srcRev = srcRev, srcSize = srcSize, srcDesc = srcDesc, createdAt = createdAt)
    }

    def delete(id: Long)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Int] = withSQL {
        deleteFrom(AlbumSrc).where.eq(column.id, id)
    }.update().future()

    def deleteByAlbumId(albumId: Long)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Int] = withSQL {
        deleteFrom(AlbumSrc).where.eq(column.albumId, albumId)
    }.update().future()

    def deleteByUserId(albumId: Long)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Int] = withSQL {
        deleteFrom(AlbumSrc).where.eq(column.albumId, albumId)
    }.update().future()

    def findByAlbumId(albumId: Long)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[List[AlbumSrc]] = withSQL {
        selectFrom(AlbumSrc as albums).where.eq(albums.albumId, albumId)
    }.map(AlbumSrc(albums)).list().future()

}