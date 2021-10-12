package joyea_share.action.leaderboard.util

import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

object LeaderboardUtil {

    def generateDateRange(year: Int, month: Int): (LocalDate, LocalDate) = {
        val startDate = LocalDate.of(year, if (month == 0) 1 else month, 1)
        val endDate = if (month == 0) {
            LocalDate.of(year, 12, 1).`with`(TemporalAdjusters.lastDayOfMonth())
        } else {
            startDate.`with`(TemporalAdjusters.lastDayOfMonth())
        }
        (startDate, endDate)
    }
}
