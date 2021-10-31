package joyea_share.model

import scalikejdbc._
import scalikejdbc.async._

import scala.concurrent.Future

case class DirWord(
                    id: Int,
                    neid: String,
                    wordId: String,
                    wordName: String
                  )


object DirWord extends SQLSyntaxSupport[DirWord] with ShortenedNames {

  lazy val dw: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[DirWord], DirWord] = DirWord.syntax("dw")

  override def columnNames: Seq[String] = Seq("id", "neid", "word_id", "word_name")

  def apply(ju: SyntaxProvider[DirWord])(rs: WrappedResultSet): DirWord = apply(dw.resultName)(rs)

  def apply(rn: ResultName[DirWord])(rs: WrappedResultSet): DirWord = autoConstruct(rs, rn)

  def opt(s: SyntaxProvider[DirWord])(rs: WrappedResultSet): Option[DirWord] =
    rs.stringOpt(s.resultName.id).map(_ => apply(s.resultName)(rs))


  def deleteDirWord(neid:String)(implicit session: AsyncDBSession = AsyncDB.sharedSession): Future[Int] = {
    withSQL {
      deleteFrom(DirWord)
        .where.eq(column.neid, neid)
    }.update().future()
  }

  def create(dirWordList: Seq[DirWord])(implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[Int] = {
    if (dirWordList.nonEmpty) {
      val insertCsv = dirWordList.map(src => {
        Seq(
          src.neid,
          src.wordId,
          src.wordName
        )
      })
      withSQL {
        insertInto(DirWord)
          .columns(
            column.neid,
            column.wordId,
            column.wordName
          ).multipleValues(
          insertCsv: _*
        )
      }.update().future()
    } else {
      Future(0)
    }
  }

  def findByNeid(neid: String)
                (implicit session: AsyncDBSession = AsyncDB.sharedSession, cxt: EC = ECGlobal): Future[List[DirWord]] = withSQL {
    selectFrom(DirWord as dw).where.eq(dw.neid, neid)
  }.map(DirWord(dw)).list().future()

}