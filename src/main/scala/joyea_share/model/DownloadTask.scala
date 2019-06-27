package joyea_share.model

import joyea_share.module.download.DownloadTask
import scalikejdbc._
import async._

object DownloadTask extends SQLSyntaxSupport[DownloadTask] with ShortenedNames {

    lazy val dt: scalikejdbc.QuerySQLSyntaxProvider[scalikejdbc.SQLSyntaxSupport[DownloadTask], DownloadTask] = DownloadTask.syntax("dt")

    override def columnNames: Seq[String] = Seq("id", "download_role_name", "download_role_id", "start_time", "finish_time")

    def apply(as: SyntaxProvider[DownloadTask])(rs: WrappedResultSet): DownloadTask = apply(as.resultName)(rs)

    def apply(as: ResultName[DownloadTask])(rs: WrappedResultSet): DownloadTask = {
        new DownloadTask(
            id = rs.get[String](as.id),
            downloadRoleName = rs.get[String](as.downloadRoleName),
            downloadRoleId = rs.get[Long](as.downloadRoleId),
            startTime = rs.get[java.util.Date](as.startTime),
            finishTime = rs.get[java.util.Date](as.finishTime)
        )
    }


}
