package jp.co.who.cop.c16

object C16 {
  val fruit = List("apples", "oranges", "pears")

  def main(args: Array[String]): Unit = {
    val l = ControllList.isort(List(10, 1, 2, 3))
  }
}

object ControllList {
  def isort(xs: List[Int]): List[Int] =
    if (xs.isEmpty) Nil
    else insert(xs.head, isort(xs.tail))

  def insert(x: Int, xs: List[Int]): List[Int] =
    if (xs.isEmpty || x <= xs.head) x :: xs
    else xs.head :: insert(x, xs.tail)
}
