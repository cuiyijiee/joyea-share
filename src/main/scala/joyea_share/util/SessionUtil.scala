package joyea_share.util

import xitrum.Action

object SessionUtil {


    def getUserName(action:Action): String = {
        action.sessiono[String]("user_name").getOrElse("")
    }

    def getUserId(action:Action): String = {
        action.sessiono[String]("user_id").getOrElse("")
    }

    def getSessionId(action:Action): String = {
        action.sessiono[String]("lenovo_session").getOrElse("")
    }

}
