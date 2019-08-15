package jp.co.who.cop.c1

import java.math.BigInteger

//import org.scalatest._

class C1 {

  def cap_1_1_0(): Unit = {
    var capital = Map("Map" -> "Washington", "France" -> "Paris")
    capital += ("Japan" -> "Tokyo")
    println(capital("France"))
  }

  def cap_1_1_1(): Unit  = {
    def factorial(x: BigInteger): BigInteger = {
      if (x == BigInteger.ZERO)
        BigInteger.ONE
      else
        x.multiply(factorial(x.subtract(BigInteger.ONE)))
    }
  }

  def cap_1_1_2(): Unit  = {
  }

  def cap_1_2_2(): Unit  = {
    val xs = 1 to 3
    val it = xs.iterator
    //eventually { it.next() shouldBe 3 }
  }

  class MyClass(var index: Int, var name: String) {}

  def cap_1_3_2(): Unit  = {
    val c = new MyClass(1,  "name")
    println(c.index + c.name)

  }
}

object C1 {

  @Override
  def main(array: Array[String]): Unit  = {
    val c1 = new C1
    val c = new c1.MyClass(1, "name")
    println(c.index)
  }
}