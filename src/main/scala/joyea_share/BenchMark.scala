package joyea_share

object BenchMark {

  def main(args: Array[String]): Unit = {
    val fileName = "123.txt"
    println(fileName.substring(fileName.lastIndexOf(".") + 1))
  }

  def test(): Unit = {


    Thread.sleep(2000)
  }
}
