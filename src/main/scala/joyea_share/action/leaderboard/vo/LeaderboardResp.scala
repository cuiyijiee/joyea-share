package joyea_share.action.leaderboard.vo

import joyea_share.model.JoyeaUser

case class LeaderboardResp(
                            user: JoyeaUser,
                            value: Long
                          )