package joyea_share.model

import java.sql.Timestamp
import java.time.{LocalDate, OffsetDateTime, OffsetTime}

import scalikejdbc._
import async._
import com.json.JsonObject
import joyea_share.vo.req.AlbumSortType

import scala.concurrent.{ExecutionContext, Future}

case class Album(
                  albumId: Long,
                  userId: String,
                  userName: String,
                  albumName: String,
                  albumDesc: Option[String],
                  shared: Boolean,
                  shareCoverNeid: Option[Long],
                  shareLocalCoverId: Option[String],
                  shareDesc: Option[String],
                  referNum: Long = 0,
                  downloadNum: Long = 0,
                  likeNum: Long = 0,
                  createdAt: OffsetDateTime,
                  updatedAt: Option[OffsetDateTime],
                  hangyeTagId: Int = -1,
                  xianbieTagId: Int = -1,
                  jixingTagId: Int = -1,
                  jieduanTagId: Int = -1,
                  shichangTagId: Int = -1,
                  copyFrom: Option[Long] = None,
                  menuId: Option[Long]
                ) extends ShortenedNames {

    def save(): Future[Album] = Album.save(this)

    def toJson: JsonObject = new JsonObject()
      .add("album_id", this.albumId)
      .add("user_id", this.userId)
      .add("user_name", this.userName)
      .add("album_name", this.albumName)
      .add("album_desc", this.albumDesc.getOrElse(""))
      .add("shared", this.shared)
      .add("share_cover_neid", this.shareCoverNeid.getOrElse(-1L))
      .add("share_local_cover_id", this.shareLocalCoverId.getOrElse(""))
      .add("share_desc", this.shareDesc.getOrElse(""))
      .add("refer_num", this.referNum)
      .add("download_num", this.downloadNum)
      .add("like_num", this.likeNum)
      .add("hangye_tag_id", this.hangyeTagId)
      .add("xianbie_tag_id", this.xianbieTagId)
      .add("jixing_tag_id", this.jixingTagId)
      .add("jieduan_tag_id", this.jieduanTagId)
      .add("shichang_tag_id", this.shichangTagId)
      .add("copy_from", this.copyFrom.getOrElse(-1L))
      .add("created_at", this.createdAt.toString)
      .add("updated_at", this.updatedAt.getOrElse(this.createdAt).toString)
}


object Album extends SQLSyntaxSupport[Album] with ShortenedNames {

    implicit val ctx: EC = ExecutionContext.Implicits.global

    lazy val a: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[Album], Album] = Album.syntax("a")

    lazy val ju: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[JoyeaUser], JoyeaUser] = JoyeaUser.ju

    override lazy val columns: Seq[String] = autoColumns[Album]()

    def apply(a: SyntaxProvider[Album])(rs: WrappedResultSet): Album = apply(a.resultName)(rs)

    def apply(a: ResultName[Album])(rs: WrappedResultSet): Album = autoConstruct(rs, a)

    def opt(s: SyntaxProvider[Album])(rs: WrappedResultSet): Option[Album] =
        rs.longOpt(s.resultName.userId).map(_ => apply(s.resultName)(rs))

    def save(album: Album, updateAt: Option[Timestamp] = Some(new Timestamp(System.currentTimeMillis())))
            (implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Album] = withSQL {
        update(Album).set(
            column.userId -> album.userId,
            column.userName -> album.userName,
            column.albumName -> album.albumName,
            column.albumDesc -> album.albumDesc,
            column.shared -> album.shared,
            column.referNum -> album.referNum,
            column.downloadNum -> album.downloadNum,
            column.likeNum -> album.likeNum,
            column.shareCoverNeid -> album.shareCoverNeid,
            column.shareLocalCoverId -> album.shareLocalCoverId,
            column.hangyeTagId -> album.hangyeTagId,
            column.xianbieTagId -> album.xianbieTagId,
            column.jixingTagId -> album.jixingTagId,
            column.jieduanTagId -> album.jieduanTagId,
            column.shichangTagId -> album.shichangTagId,
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

    def create(userId: String, userName: String, albumName: String, albumDesc: Option[String],
               shared: Boolean = false, referNum: Long = 0, downloadNum: Int = 0, likeNum: Int = 0,
               hangyeTagId: Int = -1, xianbieTagId: Int = -1, jixingTagId: Int = -1, jieduanTagId: Int = -1, shichangTagId: Int = -1,
               createdAt: OffsetDateTime = OffsetDateTime.now(), updatedAt: Option[OffsetDateTime] = None, copyFrom: Option[Long] = None, menuId: Option[Long] = Some(0))
              (implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Album] = {
        for {
            albumId <- withSQL {
                insertInto(Album).namedValues(
                    column.userId -> userId,
                    column.userName -> userName,
                    column.albumName -> albumName,
                    column.albumDesc -> albumDesc,
                    column.shared -> shared,
                    column.downloadNum -> downloadNum,
                    column.likeNum -> likeNum,
                    column.referNum -> referNum,
                    column.hangyeTagId -> hangyeTagId,
                    column.xianbieTagId -> xianbieTagId,
                    column.jixingTagId -> jixingTagId,
                    column.jieduanTagId -> jieduanTagId,
                    column.shichangTagId -> shichangTagId,
                    column.createdAt -> createdAt,
                    column.updatedAt -> updatedAt,
                    column.copyFrom -> copyFrom,
                    column.menuId -> menuId,
                )
            }.updateAndReturnGeneratedKey().future()
        } yield new Album(albumId = albumId, userId = userId, userName = userName, albumName = albumName,
            albumDesc = albumDesc, shared = shared, shareCoverNeid = None, shareLocalCoverId = None, shareDesc = None,
            createdAt = createdAt, updatedAt = updatedAt,
            hangyeTagId = hangyeTagId, xianbieTagId = xianbieTagId, jixingTagId = jixingTagId, jieduanTagId = jieduanTagId, shichangTagId = shichangTagId,
            menuId = menuId
        )
    }

    def delete(albumId: Long)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Int] = withSQL {
        deleteFrom(Album).where.eq(column.albumId, albumId)
    }.update().future()

    def shareAlbum(albumId: Long, updatedAt: Option[Timestamp] = Option(new Timestamp(System.currentTimeMillis())))
                  (implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Int] = withSQL {
        update(Album).set(
            column.shared -> true,
            column.updatedAt -> updatedAt
        ).where.eq(column.albumId, albumId)
    }.update().future()

    def unShared(albumId: Long, updatedAt: Option[Timestamp] = Option(new Timestamp(System.currentTimeMillis())))
                (implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Int] = withSQL {
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

    def addLike(albumId: Long)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Int] = this.synchronized {
        withSQL {
            update(Album).set(sqls"""${Album.column.likeNum} = ${Album.column.likeNum} + 1""").where.eq(column.albumId, albumId)
        }.update().future()
    }

    def removeRefer(albumId: Long)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Int] = withSQL {
        update(Album).set(sqls"""${Album.column.referNum} = ${Album.column.referNum} - 1""").where.eq(column.albumId, albumId)
    }.update().future()

    def addDownload(albumId: Long)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Boolean] = withSQL {
        update(Album).set(sqls"""${Album.column.downloadNum} = ${Album.column.downloadNum} + 1""").where.eq(column.albumId, albumId)
    }.update().future().map(_ >= 1)

    def searchByName(keyword: String)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[List[Album]] = withSQL {
        selectFrom(Album as a).where.like(column.albumName, s"%$keyword%")
    }.map(Album(a)).list().future()

    //hangyeTagId = hangyeTagId, xianbieTagId = xianbieTagId, jixingTagId = jixingTagId, jieduanTagId = jieduanTagId, shichangId = shichangId
    def pageListAlbum(curPage: Int, pageSize: Int, shared: Option[Boolean], sortType: AlbumSortType.AlbumSortType,
                      hangyeTagId: Int = -1, xianbieTagId: Int = -1, jixingTagId: Int = -1, jieduanTagId: Int = -1, shichangTagId: Int = -1)
                     (implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[List[Album]] = {

        if (shared.isDefined) {
            withSQL {
                selectFrom(Album as a).where.eq(column.shared, shared.get)
                  .append(if (hangyeTagId == -1) sqls.empty else sqls.and(sqls.eq(column.hangyeTagId, hangyeTagId)))
                  .append(if (xianbieTagId == -1) sqls.empty else sqls.and(sqls.eq(column.xianbieTagId, xianbieTagId)))
                  .append(if (jixingTagId == -1) sqls.empty else sqls.and(sqls.eq(column.jixingTagId, jixingTagId)))
                  .append(if (jieduanTagId == -1) sqls.empty else sqls.and(sqls.eq(column.jieduanTagId, jieduanTagId)))
                  .append(if (shichangTagId == -1) sqls.empty else sqls.and(sqls.eq(column.shichangTagId, shichangTagId)))
                  .orderBy(
                      if (sortType == AlbumSortType.LikeNum) column.likeNum else column.createdAt
                  ).desc.limit(pageSize).offset((curPage - 1) * pageSize)
            }.map(Album(a)).list().future()
        } else {
            withSQL {
                selectFrom(Album as a)
                  .where.ge(column.albumId, 0)
                  .append(if (hangyeTagId == -1) sqls.empty else sqls.and(sqls.eq(column.hangyeTagId, hangyeTagId)))
                  .append(if (xianbieTagId == -1) sqls.empty else sqls.and(sqls.eq(column.xianbieTagId, xianbieTagId)))
                  .append(if (jixingTagId == -1) sqls.empty else sqls.and(sqls.eq(column.jixingTagId, jixingTagId)))
                  .append(if (jieduanTagId == -1) sqls.empty else sqls.and(sqls.eq(column.jieduanTagId, jieduanTagId)))
                  .append(if (shichangTagId == -1) sqls.empty else sqls.and(sqls.eq(column.shichangTagId, shichangTagId)))
                  .orderBy(
                      if (sortType == AlbumSortType.LikeNum) column.likeNum else column.createdAt
                  ).desc.limit(pageSize).offset((curPage - 1) * pageSize)
            }.map(Album(a)).list().future()
        }
    }

    def switchShare(albumName: String, coverId: Option[Long], localCoverId: Option[String], shareDesc: Option[String], share: Boolean, albumId: Long,
                    hangyeTagId: Int = -1, xianbieTagId: Int = -1, jixingTagId: Int = -1, jieduanTagId: Int = -1, shichangTagId: Int = -1)
                   (implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Boolean] = withSQL {
        if (share) {
            update(Album).set(
                column.albumName -> albumName,
                column.shareCoverNeid -> coverId,
                column.shareLocalCoverId -> localCoverId,
                column.shareDesc -> shareDesc,
                column.shared -> share,
                column.hangyeTagId -> hangyeTagId,
                column.xianbieTagId -> xianbieTagId,
                column.jixingTagId -> jixingTagId,
                column.jieduanTagId -> jieduanTagId,
                column.shichangTagId -> shichangTagId,
            ).where.eq(column.albumId, albumId)
        } else {
            update(Album).set(
                column.shared -> share,
            ).where.eq(column.albumId, albumId)
        }
    }.update().future().map(_ => true)


    def findAllCopied(userId: String)
                     (implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[List[Album]] = {
        withSQL {
            selectFrom(Album as a)
              .where.eq(column.userId, userId).and.append(sqls.isNotNull(column.copyFrom))
        }.map(Album(a)).list().future()
    }

    def findSharedWithDateLimit(startDate: LocalDate, endDate: LocalDate)
                               (implicit session: AsyncDBSession = AsyncDB.sharedSession): Future[List[(JoyeaUser, Long)]] = {
        withSQL {
            select.from[JoyeaUser](JoyeaUser as ju)
              .leftJoin(Album as a).on(ju.joyeaId, a.userId).append(
                sqls.and.ge(a.createdAt, startDate).and.le(a.createdAt, endDate).and.eq(a.shared, true)
            )
        }.one(JoyeaUser(ju))
          .toMany(Album.opt(a))
          .map((user, record) => {
              (user, record.length.toLong)
          }).list().future()
    }

    def setMenu(albumId: Long, menuId: Long)
               (implicit session: AsyncDBSession = AsyncDB.sharedSession): Future[Boolean] = {
        withSQL {
            update(Album as a).set(
                column.menuId -> menuId
            ).where.eq(column.albumId, albumId)
        }.update().future().map(_ > 0)
    }
}

//
