package jp.co.who.cop.c1

import java.math.BigInteger

//import org.scalatest._

class C1 {

  def cap_1_1_0() = {
    var capital = Map("Map" -> "Washington", "France" -> "Paris")
    capital += ("Japan" -> "Tokyo")
    println(capital("France"))
  }

  def cap_1_1_1() = {
    def factorial(x: BigInteger): BigInteger = {
      if (x == BigInteger.ZERO)
        BigInteger.ONE
      else
        x.multiply(factorial(x.subtract(BigInteger.ONE)))
    }
  }

  def cap_1_1_2() = {
  }

  def cap_1_2_2() = {
    val xs = 1 to 3
    val it = xs.iterator
    //eventually { it.next() shouldBe 3 }
  }

  class MyClass(var index: Int, var name: String) {}

  def cap_1_3_2() = {
    val c = new MyClass(1,  "name")
    println(c.index + c.name)

  }
}
