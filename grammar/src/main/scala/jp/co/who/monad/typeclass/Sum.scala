package jp.co.who.monad.typeclass

object Sum {

  // 型クラスの trait
  trait Sum[T] {
    def sumx(x: T): Int
  }

  // implicit conversion 内で実行してくれる関数の定義
  implicit def Sum: Sum[List[Int]] = new Sum[List[Int]] {
    override def sumx(x: List[Int]): Int = x.foldLeft(0)( _ + _)
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