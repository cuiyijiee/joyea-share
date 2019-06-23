package joyea_share.util

import xitrum.Action

object SessionUtil {


    def getUserName(action:Action): String = {
        action.sessiono[String]("user_name").getOrElse("")
    }

    def getUserId(action:Action): Long = {
        action.sessiono[Long]("user_id").getOrElse(-1L)
    }

    def getSessionId(action:Action): String = {
        action.sessiono[String]("lenovo_session").getOrElse("")
    }

}
