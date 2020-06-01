package joyea_share.vo.req

case class LoginReq(
                     user:String,
                     see:Option[Boolean],
                     pwd:String
                   )
