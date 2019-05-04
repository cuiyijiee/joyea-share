package joyea_share.model

import java.sql.Timestamp

import scalikejdbc._
import async._
import com.json.JsonObject

import scala.concurrent.Future

case class SrcCollect(
                       id: Long,
                       srcNeid: Long,
                       userId: Long,
                       srcPath: String,
                       srcType: String,
                       srcHash: String,
                       srcRev: String,
                       srcSize: String,
                       createdAt: Timestamp
                     ) extends ShortenedNames {

  def toJson: JsonObject = new JsonObject()
    .add("id", this.id)
    .add("neid", this.srcNeid)
    .add("user_id", this.userId)
    .add("path", this.srcPath)
    .add("type", this.srcType)
    .add("rev", this.srcRev)
    .add("size", this.srcSize)
    .add("created_at", this.createdAt.getTime)
}

object SrcCollect extends SQLSyntaxSupport[SrcCollect] with ShortenedNames {

  lazy val sc: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[SrcCollect], SrcCollect] = SrcCollect.syntax("sc")

  override def columnNames: Seq[String] = Seq("id", "src_neid", "user_id", "src_path", "src_type", "src_hash", "src_rev", "src_size", "created_at")

  def apply(sc: SyntaxProvider[SrcCollect])(rs: WrappedResultSet): SrcCollect = apply(sc.resultName)(rs)

  def apply(p: ResultName[SrcCollect])(rs: WrappedResultSet): SrcCollect = {
    new SrcCollect(
      id = rs.get[Long](p.id),
      srcNeid = rs.get[Long](p.srcNeid),
      userId = rs.get[Long](p.userId),
      srcPath = rs.get[String](p.srcPath),
      srcType = rs.get[String](p.srcType),
      srcHash = rs.get[String](p.srcHash),
      srcRev = rs.get[String](p.srcRev),
      srcSize = rs.get[String](p.srcSize),
      createdAt = rs.get[Timestamp](p.createdAt)
    )
  }

  def create(srcNeid: Long, userId: Long, srcPath: String, srcType: String, srcHash: String, srcRev: String,
             srcSize: String, createdAt: Timestamp = new Timestamp(System.currentTimeMillis()))
            (implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[SrcCollect] = {

    for {
      id <- withSQL {
        insertInto(SrcCollect).namedValues(
          column.srcNeid -> srcNeid,
          column.userId -> userId,
          column.srcPath -> srcPath,
          column.srcType -> srcType,
          column.srcHash -> srcHash,
          column.srcRev -> srcRev,
          column.srcSize -> srcSize,
          column.createdAt -> createdAt
        )
      }.updateAndReturnGeneratedKey().future()
    } yield SrcCollect(id = id, srcNeid = srcNeid, userId = userId, srcPath = srcPath, srcType = srcType,
      srcHash = srcHash, srcRev = srcRev, srcSize = srcSize, createdAt = createdAt)
  }

  def delete(id: Long)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Int] = withSQL {
    deleteFrom(SrcCollect).where.eq(column.id, id)
  }.update().future()

  /**
    * 查询用户是否已经收藏该资源
    *
    * @param userId
    * @param neid
    * @param session
    * @param cxt
    * @return
    */
  def findByUserIdAndNeid(userId: Long, neid: Long)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Option[SrcCollect]] = withSQL {
    selectFrom(SrcCollect as sc).where.eq(column.userId, userId).and.eq(column.srcNeid, neid)
  }.map(SrcCollect(sc)).single().future()


  /**
    * 取消收藏某一个资源
    *
    * @param userId
    * @param neid
    * @param session
    * @param cxt
    * @return
    */
  def deleteByUserIdAndNeid(userId: Long, neid: Long)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Int] = withSQL {
    deleteFrom(SrcCollect).where.eq(column.userId, userId).and.eq(column.srcNeid, neid)
  }.update().future()


  /**
    * 分页查询查找
    *
    * @param curPage
    * @param pageSize
    * @param userId
    * @param session
    * @param cxt
    * @return
    */
  def pageFindByUserId(curPage: Int, pageSize: Int, userId: Long)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[List[SrcCollect]] = withSQL {
    selectFrom(SrcCollect as sc).where.eq(sc.userId, userId).orderBy(sc.createdAt).limit(pageSize).offset((curPage - 1) * pageSize)
  }.map(SrcCollect(sc)).list().future()

  /**
    * 查找用户总的收藏数
    *
    * @param userId
    * @param session
    * @param cxt
    * @return
    */
  def countByUserId(userId: Long)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Long] = withSQL {
    select(sqls.count).from(SrcCollect as sc).where.eq(sc.userId, userId)
  }.map(_.long(1)).single().future().map(_.get)

}
