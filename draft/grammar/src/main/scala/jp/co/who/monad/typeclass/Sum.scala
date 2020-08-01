package jp.co.who.monad.typeclass

object Sum {

  // 型クラスの trait
  trait Sum[T] {
    def sumx(x: T): Int
  }

  // implicit conversion 内で実行してくれる関数の定義
  implicit def Sum(implicit mi: Monoid[Int]): Sum[List[Int]] = new Sum[List[Int]] {
    override def sumx(x: List[Int]): Int = x.foldLeft(mi.mzero)(mi.mappend)
  }

  // implicit class の基底クラス
  trait SumOps[T] {
    def self: T
    implicit def ins: Sum[T]

    def summ = ins.sumx(self)
  }

  // implicit conversion を行う実体となるクラス
  implicit class ToSum[T](v: T)(implicit i:Sum[T]) extends SumOps[T] {
    override implicit def ins: Sum[T] = i

    override def self: T = v
  }

}

// Monoid
trait Monoid[A] {
  def mappend(a: A, b: A): A
  def mzero: A
}
object Monoid {

  // Int の総和
  implicit val IntMonoid = new Monoid[Int] {
    def mappend(a: Int, b: Int): Int = a + b
    def mzero: Int = 0
  }

  // Int の総積
  val multiMonoid: Monoid[Int] = new Monoid[Int] {
    def mappend(a: Int, b: Int): Int = a * b
    def mzero: Int = 1
  }

  implicit val StringMonoid = new Monoid[String] {
    def mappend(a: String, b: String): String = a + b
    def mzero: String = ""
  }

  def sum[A: Monoid](xs: List[A]): A = {
    val m = implicitly[Monoid[A]]
    xs.foldLeft(m.mzero)(m.mappend)
  }
}