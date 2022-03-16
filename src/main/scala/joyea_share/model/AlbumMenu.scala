package joyea_share.model


import java.time.OffsetDateTime

import scalikejdbc._
import scalikejdbc.async._

import scala.concurrent.Future

case class AlbumMenu(
                      id: Long,
                      userId: String,
                      name: String,
                      createdAt: OffsetDateTime
                    )


object AlbumMenu extends SQLSyntaxSupport[AlbumMenu] with ShortenedNames {

    lazy val am: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[AlbumMenu], AlbumMenu] = AlbumMenu.syntax("am")

    override lazy val columns: collection.Seq[String] = autoColumns[AlbumMenu]()

    def apply(a: SyntaxProvider[AlbumMenu])(rs: WrappedResultSet): AlbumMenu = apply(a.resultName)(rs)

    def apply(a: ResultName[AlbumMenu])(rs: WrappedResultSet): AlbumMenu = autoConstruct(rs, a)

    def opt(s: SyntaxProvider[AlbumMenu])(rs: WrappedResultSet): Option[AlbumMenu] =
        rs.longOpt(s.resultName.id).map(_ => apply(s.resultName)(rs))


    def create(joyeaId: String, menuName: String)
              (implicit session: AsyncDBSession = AsyncDB.sharedSession): Future[Long] = {
        withSQL {
            insert.into(AlbumMenu)
              .namedValues(
                  column.userId -> joyeaId,
                  column.name -> menuName,
                  column.createdAt -> sqls.currentTimestamp
              )
        }.updateAndReturnGeneratedKey().future()
    }


    def delete(id: Long)
              (implicit session: AsyncDBSession = AsyncDB.sharedSession): Future[Int] = {
        withSQL {
            deleteFrom(AlbumMenu)
              .where.eq(column.id, id)
        }.update().future()
    }

    def find(id: Long)
                    (implicit session: AsyncDBSession = AsyncDB.sharedSession): Future[Option[AlbumMenu]] = {
        withSQL {
            selectFrom(AlbumMenu as am)
              .where.eq(column.id, id)
        }.map(AlbumMenu(am)).single().future()
    }

    def findByUserId(joyeaId: String)
                    (implicit session: AsyncDBSession = AsyncDB.sharedSession): Future[Seq[AlbumMenu]] = {
        withSQL {
            selectFrom(AlbumMenu as am)
              .where.eq(column.userId, joyeaId)
        }.map(AlbumMenu(am)).list().future()
    }

    def findByUserIdAndMenuName(joyeaId: String, menuName: String)
                               (implicit session: AsyncDBSession = AsyncDB.sharedSession): Future[Option[AlbumMenu]] = {
        withSQL {
            selectFrom(AlbumMenu as am)
              .where.eq(column.userId, joyeaId)
              .and.eq(column.name, menuName)
        }.map(AlbumMenu(am)).single().future()

    }

    def renameMenu(id: Long, newName: String)
                  (implicit session: AsyncDBSession = AsyncDB.sharedSession): Future[Int] = {
        withSQL {
            update(AlbumMenu as am)
              .set(
                  am.name -> newName
              ).where.eq(am.id, id)
        }.update().future()
    }


}