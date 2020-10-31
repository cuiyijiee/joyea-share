package joyea_share.model

import java.sql.Timestamp

import scalikejdbc._
import scalikejdbc.async._

import scala.concurrent.Future

case class JoyeaUser(
                      id: Long,
                      joyeaId: String,
                      joyeaName: String,
                      password: String,
                      position: String, //职务
                      department: String, //部门
                      isAdmin: Boolean = false,
                      updateAt: Timestamp
                    )

object JoyeaUser extends SQLSyntaxSupport[JoyeaUser] with ShortenedNames {

  lazy val ju: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[JoyeaUser], JoyeaUser] = JoyeaUser.syntax("ju")

  override def columnNames: Seq[String] = Seq("id", "joyea_id", "joyea_name", "password", "position", "department", "update_at", "is_admin")

  def apply(ju: SyntaxProvider[JoyeaUser])(rs: WrappedResultSet): JoyeaUser = apply(ju.resultName)(rs)

  def apply(rn: ResultName[JoyeaUser])(rs: WrappedResultSet): JoyeaUser = autoConstruct(rs, rn)

  def opt(s: SyntaxProvider[JoyeaUser])(rs: WrappedResultSet): Option[JoyeaUser] =
    rs.longOpt(s.resultName.joyeaId).map(_ => apply(s.resultName)(rs))

  def create(joyeaId: String, joyeaName: String, password: String, position: String,
             isAdmin: Boolean = false,
             department: String, updateAt: Timestamp = new Timestamp(System.currentTimeMillis()))
            (implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[JoyeaUser] = {
    for (userId <- withSQL {
      insert.into(JoyeaUser).namedValues(
        column.joyeaId -> joyeaId,
        column.joyeaName -> joyeaName,
        column.password -> password,
        column.position -> position,
        column.department -> department,
        column.updateAt -> updateAt,
        column.isAdmin -> isAdmin,
      )
    }.updateAndReturnGeneratedKey().future()
         ) yield new JoyeaUser(id = userId, joyeaId = joyeaId, joyeaName = joyeaName, password = password, isAdmin = isAdmin, position = position, department = department, updateAt = updateAt)
  }

  def findByJoyeaId(joyeaId: String)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Option[JoyeaUser]] = {
    withSQL {
      select.from(JoyeaUser as ju)
        .where.eq(ju.joyeaId, joyeaId)
    }.map(JoyeaUser(ju)).single().future()
  }

  def changePwd(uid: Long, newPwd: String)
               (implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Boolean] = {
    withSQL {
      update(JoyeaUser).set(column.password -> newPwd).where.eq(column.id, uid)
    }.update().future().map(_ > 0)
  }
}
