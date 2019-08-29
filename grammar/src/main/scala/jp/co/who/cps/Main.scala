package jp.co.who.cps

object Main extends App {

  def add1(a: Int): Int = a + 1
  def add3(b: Int): Int = b + 3

  val value = args.head
  val ret = Cps.f(Something(value))
  println(ret)

  val c = 10
  println(Cps.convert(c, add1))
  println(Cps.convert(c, add3))
}
