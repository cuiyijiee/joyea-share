package joyea_share.model

import java.time.OffsetDateTime

import scalikejdbc._
import async._
import com.json.JsonObject
import org.cuje.lib.TimeUtil

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
                     srcFileName: String,
                     srcBytes: Long,
                     createdAt: OffsetDateTime = OffsetDateTime.now()
                   ) extends ShortenedNames {

    def toJson: JsonObject = new JsonObject()
      .add("id", this.id)
      .add("neid", this.srcNeid)
      .add("album_id", this.albumId)
      .add("path", this.srcPath)
      .add("mime_type", this.srcType)
      .add("rev", this.srcRev)
      .add("hash", this.srcHash)
      .add("size", this.srcSize)
      .add("desc", this.srcDesc)
      .add("bytes", this.srcBytes)
      .add("filename", this.srcFileName)
      .add("created_at", TimeUtil.offsetDataTimeToTimestamp(this.createdAt))
}

object AlbumSrc extends SQLSyntaxSupport[AlbumSrc] with ShortenedNames {

    lazy val albums: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[AlbumSrc], AlbumSrc] = AlbumSrc.syntax("albums")

    override def columnNames: Seq[String] = Seq("id", "album_id", "src_neid", "src_path", "src_type", "src_hash", "src_rev", "src_size", "src_file_name", "src_bytes", "src_desc", "created_at")

    def apply(as: SyntaxProvider[AlbumSrc])(rs: WrappedResultSet): AlbumSrc = apply(as.resultName)(rs)

    def apply(as: ResultName[AlbumSrc])(rs: WrappedResultSet): AlbumSrc = autoConstruct(rs, as)

    def opt(s: SyntaxProvider[AlbumSrc])(rs: WrappedResultSet): Option[AlbumSrc] =
        rs.longOpt(s.resultName.id).map(_ => apply(s.resultName)(rs))

    def createMany(albumSrcList: Seq[AlbumSrc])
                  (implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Int] = {
        if (albumSrcList.nonEmpty) {
            val insertCsv = albumSrcList.map(src => {
                Seq(
                    src.srcNeid,
                    src.albumId,
                    src.srcPath,
                    src.srcType,
                    src.srcHash,
                    src.srcRev,
                    src.srcSize,
                    src.srcDesc,
                    src.srcFileName,
                    src.srcBytes,
                    sqls.currentTimestamp
                )
            })
            withSQL {
                insertInto(AlbumSrc)
                  .columns(
                      column.srcNeid,
                      column.albumId,
                      column.srcPath,
                      column.srcType,
                      column.srcHash,
                      column.srcRev,
                      column.srcSize,
                      column.srcDesc,
                      column.srcFileName,
                      column.srcBytes,
                      column.createdAt
                  ).multipleValues(
                    insertCsv: _*
                )
            }.update().future()
        } else {
            Future(0)
        }
    }

    def create(srcNeid: Long, albumId: Long, srcPath: String, srcType: String, srcHash: String, srcRev: String,
               srcSize: String, srcDesc: String, srcFileName: String, srcBytes: Long, createdAt: OffsetDateTime = OffsetDateTime.now())
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
                    column.srcFileName -> srcFileName,
                    column.srcBytes -> srcBytes,
                    column.createdAt -> createdAt
                )
            }.updateAndReturnGeneratedKey().future()
        } yield AlbumSrc(id = id, srcNeid = srcNeid, albumId = albumId, srcPath = srcPath, srcType = srcType,
            srcHash = srcHash, srcRev = srcRev, srcSize = srcSize, srcDesc = srcDesc, srcFileName = srcFileName, srcBytes = srcBytes, createdAt = createdAt)
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

    def findByNeid(srcNeid: Long)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[List[AlbumSrc]] = withSQL {
        selectFrom(AlbumSrc as albums).where.eq(albums.srcNeid, srcNeid)
    }.map(AlbumSrc(albums)).list().future()

    def countByNeid(srcNeid: Long)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Long] = withSQL {
        select(sqls.count).from(AlbumSrc as albums).where.eq(albums.srcNeid, srcNeid)
    }.map(_.long(1)).single().future().map(_.get)

}