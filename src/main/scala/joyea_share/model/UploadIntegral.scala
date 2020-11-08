package joyea_share.model

import java.time.OffsetDateTime

import scalikejdbc._
import scalikejdbc.async._

import scala.concurrent.Future

case class UploadIntegral(
                           id: Long,
                           neid: Long,
                           creator: String,
                           integral: Long,
                           createdAt: OffsetDateTime,
                         )


object UploadIntegral extends SQLSyntaxSupport[UploadIntegral] with ShortenedNames {

    implicit val session: AsyncDBSession = AsyncDB.sharedSession
    implicit val cxt: EC = ECGlobal

    lazy val ur: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[UploadIntegral], UploadIntegral] = UploadIntegral.syntax("ui")

    override lazy val columns: Seq[String] = autoColumns[UploadIntegral]()

    val ju: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[JoyeaUser], JoyeaUser] = JoyeaUser.ju

    def apply(sc: SyntaxProvider[UploadIntegral])(rs: WrappedResultSet): UploadIntegral = apply(sc.resultName)(rs)

    def apply(p: ResultName[UploadIntegral])(rs: WrappedResultSet): UploadIntegral = autoConstruct(rs, p)

    def create(neid: Long, creator: String, integral: Long, createdAt: OffsetDateTime = OffsetDateTime.now())
              (implicit session: AsyncDBSession = AsyncDB.sharedSession): Future[Long] = {
        withSQL {
            insert.into(UploadIntegral).namedValues(
                column.neid -> neid,
                column.creator -> creator,
                column.integral -> integral,
                column.createdAt -> createdAt,
            )
        }.updateAndReturnGeneratedKey().future()
    }



}