package joyea_share.module.download

trait DownloadListener {

    //开始下载
    def onStart(taskId: String, total: Int): Unit

    def onNext(taskId: String, progress: Int, total: Int): Unit

    def onFinish(taskId: String, success: Int, failed: Int, total: Int): Unit
}
