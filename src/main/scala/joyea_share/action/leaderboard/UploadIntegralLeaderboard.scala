package joyea_share.action.leaderboard

import joyea_share.action.BaseAction
import joyea_share.action.leaderboard.util.LeaderboardUtil
import joyea_share.action.leaderboard.vo.{LeaderboardReq, LeaderboardResp}
import joyea_share.model.{JoyeaUser, UploadIntegral}
import xitrum.annotation.POST

@POST("api/v1/leaderboard/uploadIntegral")
class UploadIntegralLeaderboard extends BaseAction[LeaderboardReq] {
    override def safeExecute(req: LeaderboardReq): Unit = {
        val dateRange = LeaderboardUtil.generateDateRange(req.year, req.month)
        UploadIntegral.findUploadLeaderboardWithDateLimit(dateRange._1, dateRange._2)
          .onComplete(safeResponse[List[(JoyeaUser, Long)]](_, result => {
              val leaderboardResult = result.map(item => {
                  LeaderboardResp(user = item._1, value = item._2)
              })
              baseResponseSuccess(leaderboardResult)
          }))
    }
}

