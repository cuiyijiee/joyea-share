package joyea_share.util

import java.io.{BufferedOutputStream, File, FileInputStream, FileOutputStream}
import java.util.zip.{ZipEntry, ZipFile, ZipOutputStream}

object ZipUtils {


  def compressZip(pathArr: Array[String], targetPath: String): Unit = {
    val targetFile = new File(targetPath)
    if (!targetFile.getParentFile.exists()) targetFile.getParentFile.mkdirs()
    val outputStream = new FileOutputStream(targetFile)
    val bfo = new BufferedOutputStream(outputStream)
    val out = new ZipOutputStream(bfo)
    pathArr.foreach(path => {
      compressZip(out, new File(path), "")
    })
    out.closeEntry()
    out.close()
    bfo.close()
    outputStream.close()
  }

  //  def compressZip(sourcePath: String, targetPath: String): Unit = {
  //    val srcFile = new File(sourcePath)
  //    val targetFile = new File(targetPath)
  //    if (!targetFile.getParentFile.exists()) targetFile.getParentFile.mkdirs()
  //    val outputStream = new FileOutputStream(targetFile)
  //    val bfo = new BufferedOutputStream(outputStream)
  //    val out = new ZipOutputStream(bfo)
  //    compressZip(out, srcFile, "")
  //    out.close()
  //    bfo.close()
  //    outputStream.close()
  //  }

  /**
    * 生成压缩文件。 如果是文件夹，则使用递归，进行文件遍历、压缩 如果是文件，直接压缩
    *
    * @param out  输出流
    * @param file 目标文件
    * @param dir
    */
  def compressZip(out: ZipOutputStream, file: File, dir: String): Unit = {
    var dirStr: String = dir
    if (!file.exists()) return
    if (file.isDirectory) {
      val files = file.listFiles()
      //out.putNextEntry(new ZipEntry(dir + File.separator + file.getName + File.separator))
      dirStr = if (dirStr.length == 0) "" else dir + File.separator
      for (index <- 0 until files.length) compressZip(out, files(index), dirStr + files(index).getName)
    } else {
      val fis = new FileInputStream(file)
      out.putNextEntry(new ZipEntry(dirStr))
      val buffer: Array[Byte] = new Array[Byte](8192)
      var j = fis.read(buffer)
      while (j > 0) {
        out.write(buffer, 0, j)
        j = fis.read(buffer)
      }
      fis.close()
    }
  }

  /**
    * zip文件解压
    *
    * @param originFile 原始zip文件
    * @param targetFile 解压的目标目录
    * @return
    */
  def decompressZip(originFile: File, targetFile: File): Boolean = {
    var targetDir: String = targetFile.getAbsolutePath
    if (!originFile.exists() || !originFile.getAbsolutePath.endsWith(".zip")) return false
    if (!targetFile.getAbsolutePath.endsWith(File.separator)) targetDir += File.separator
    val zipFile = new ZipFile(originFile)
    var zipEntry: ZipEntry = null
    val entry = zipFile.entries()
    while (entry.hasMoreElements) {
      zipEntry = entry.nextElement()
      val fileName = zipEntry.getName
      val outFile = new File(targetDir + fileName)
      if (!outFile.getParentFile.exists()) outFile.getParentFile.mkdirs()
      if (zipEntry.isDirectory) {
        outFile.mkdirs()
      } else {
        val outputStream = new FileOutputStream(outFile)
        val inputStream = zipFile.getInputStream(zipEntry)
        val buffer: Array[Byte] = new Array[Byte](8192)
        var len: Int = inputStream.read(buffer)
        while (len != -1) {
          outputStream.write(buffer, 0, len)
          len = inputStream.read(buffer)
        }
        outputStream.close()
        inputStream.close()
      }
    }
    zipFile.close()
    true
  }
}
