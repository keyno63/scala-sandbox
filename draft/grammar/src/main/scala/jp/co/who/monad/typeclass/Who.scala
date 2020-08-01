package jp.co.who.monad.typeclass

trait Who[T] {
  def who(x: T): String
}

object Who {
  implicit def WhoInt: Who[Int] = new Who[Int] {
    def who(x: Int) = "Int"
  }

  implicit object WhoDouble extends Who[Double] {
    def who(x: Double) = "Double"
  }

  implicit def WhoString: Who[String] = (x: String) => "String"

  /*
  implicit def ToWhoOps[T](v: T)(implicit i: Who[T]): WhoOps[T] =
    new WhoOps[T] {
      def self = v;
      implicit def instance: Who[T] = i
    }

   */

  implicit class ToWhoOps[T](v: T)(implicit i: Who[T]) extends WhoOps[T] {
    override def self: T = v

    override implicit def instance: Who[T] = i
  }

}

trait WhoOps[T] {
  def self: T
  implicit def instance: Who[T]

  def whoMethod() = instance.who(self)
}
