package joyea_share.handler.interfaces

object ActionFacade {
  private val classNameTemple = "joyea_share.handler.%sHandler"
  private val classCache: scala.collection.mutable.Map[String, Class[_]] = scala.collection.mutable.Map()

  def getMethodHandler(actionName: String): IAction = {
    val className = classNameTemple.format(actionName.capitalize)
    var clazz = classCache.getOrElse(className, null)
    if (clazz == null) {
      clazz = Class.forName(className)
      classCache.put(className, clazz)
    }
    var action: IAction = null
    if (clazz != null) {
      val cons = clazz.getConstructors
      action = cons(0).newInstance().asInstanceOf[IAction]
      action.resJson.add("action", actionName)
    }
    action
  }
}
