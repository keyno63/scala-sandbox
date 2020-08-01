package jp.co.who.cps

case class Something(x:String)

object Cps {
  type A = Something
  type B = String

  def f: A => B = _.x
  def convert(a:Int, func: Int => Int): Int = {
    func(a)
  }

  def g: A => B = _.x
}
