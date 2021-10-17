package joyea_share.model

import scalikejdbc._
import scalikejdbc.async._

import scala.concurrent.Future

case class NextPlusUser(
                         id: String,
                         name: String,
                         easUserId: String,
                         imgUrl: String,
                         phone: String,
                         ytmId: String,
                         ytmOpenId: String,
                         position: String,
                         departmentId: String,
                         departmentName: String,
                         departmentType: String,
                         tenantId: String,
                         yzjOpenId: String,
                         extendInfo: String
                       )

object NextPlusUser extends SQLSyntaxSupport[NextPlusUser] with ShortenedNames {

  lazy val npu: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[NextPlusUser], NextPlusUser] = NextPlusUser.syntax("npu")

  override def columnNames: Seq[String] = Seq("id", "name", "eas_user_id", "img_url", "phone", "ytm_id", "ytm_open_id", "position", "department_id", "department_name", "department_type", "tenant_id", "yzj_open_id", "extend_info")

  def apply(ju: SyntaxProvider[NextPlusUser])(rs: WrappedResultSet): NextPlusUser = apply(npu.resultName)(rs)

  def apply(rn: ResultName[NextPlusUser])(rs: WrappedResultSet): NextPlusUser = autoConstruct(rs, rn)

  def opt(s: SyntaxProvider[NextPlusUser])(rs: WrappedResultSet): Option[NextPlusUser] =
    rs.stringOpt(s.resultName.id).map(_ => apply(s.resultName)(rs))

  def create(id: String,
             name: String,
             easUserId: String,
             imgUrl: String,
             phone: String,
             ytmId: String,
             ytmOpenId: String,
             position: String,
             departmentId: String,
             departmentName: String,
             departmentType: String,
             tenantId: String,
             yzjOpenId: String,
             extendInfo: String)
            (implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Int] = {
    withSQL {
      insert.into(NextPlusUser).namedValues(
        column.id -> id,
        column.name -> name,
        column.easUserId -> easUserId,
        column.imgUrl -> imgUrl,
        column.phone -> phone,
        column.ytmId -> ytmId,
        column.ytmOpenId -> ytmOpenId,
        column.position -> position,
        column.departmentId -> departmentId,
        column.departmentName -> departmentName,
        column.departmentType -> departmentType,
        column.tenantId -> tenantId,
        column.yzjOpenId -> yzjOpenId,
        column.extendInfo -> extendInfo,
      )
    }.update().future()
  }

  def selectByYtmId(ytmId: String)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Option[NextPlusUser]] = {
    withSQL {
      select.from(NextPlusUser as npu)
        .where.eq(npu.ytmId, ytmId)
    }.map(NextPlusUser(npu)).single().future()
  }

  def selectById(id: String)(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Option[NextPlusUser]] = {
    withSQL {
      select.from(NextPlusUser as npu)
        .where.eq(npu.id, id)
    }.map(NextPlusUser(npu)).single().future()
  }

}