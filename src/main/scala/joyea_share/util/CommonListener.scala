package joyea_share.util

trait CommonListener[T] {

  def onSuccess(obj: T): Unit


  def onError(error: String): Unit

}
