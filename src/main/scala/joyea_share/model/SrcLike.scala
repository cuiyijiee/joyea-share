package joyea_share.model

import java.sql.Timestamp

import scalikejdbc._
import async._

import scala.concurrent.Future

case class SrcLike(
                    id: Long,
                    srcNeid: Long,
                    userId: String,
                    srcPath: String,
                    srcType: String,
                    srcHash: String,
                    srcRev: String,
                    srcSize: String,
                    createdAt: Timestamp,
                    desc: Option[String]
                  ) extends ShortenedNames {

}

object SrcLike extends SQLSyntaxSupport[SrcLike] with ShortenedNames {

  lazy val sl: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[SrcLike], SrcLike] = SrcLike.syntax("sl")

  override def columnNames: Seq[String] = Seq("id", "src_neid", "user_id", "src_path", "src_type", "src_hash", "src_rev", "src_size", "created_at", "desc")

  def apply(sl: SyntaxProvider[SrcLike])(rs: WrappedResultSet): SrcLike = apply(sl.resultName)(rs)

  def apply(p: ResultName[SrcLike])(rs: WrappedResultSet): SrcLike = {
    new SrcLike(
      id = rs.get[Long](p.id),
      srcNeid = rs.get[Long](p.srcNeid),
      userId = rs.get[String](p.userId),
      srcPath = rs.get[String](p.srcPath),
      srcType = rs.get[String](p.srcType),
      srcHash = rs.get[String](p.srcHash),
      srcRev = rs.get[String](p.srcRev),
      srcSize = rs.get[String](p.srcSize),
      createdAt = rs.get[Timestamp](p.createdAt),
      desc = rs.get[Option[String]](p.desc),
    )
  }

  def create(srcNeid: Long, userId: String, srcPath: String, srcType: String, srcHash: String, srcRev: String,
             srcSize: String, createdAt: Timestamp = new Timestamp(System.currentTimeMillis()), desc: Option[String])
            (implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[SrcLike] = {

    for {
      id <- withSQL {
        insertInto(SrcLike).namedValues(
          column.srcNeid -> srcNeid,
          column.userId -> userId,
          column.srcPath -> srcPath,
          column.srcType -> srcType,
          column.srcHash -> srcHash,
          column.srcRev -> srcRev,
          column.srcSize -> srcSize,
          column.createdAt -> createdAt,
          column.desc -> desc
        )
      }.updateAndReturnGeneratedKey().future()
    } yield SrcLike(id = id, srcNeid = srcNeid, userId = userId, srcPath = srcPath, srcType = srcType,
      srcHash = srcHash, srcRev = srcRev, srcSize = srcSize, createdAt = createdAt, desc = desc)
  }

  def delete(id: Long)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Int] = withSQL {
    deleteFrom(SrcLike).where.eq(column.id, id)
  }.update().future()

}
