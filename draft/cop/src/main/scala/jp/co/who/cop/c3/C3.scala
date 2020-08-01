package jp.co.who.cop.c3

import java.math.BigInteger

class C3 {

  def cap_3_1_0(): Unit = {
    val big = new BigInteger("12345")

    val greetString = new Array[String](3)
    greetString(0) = "Hello"
    greetString(1) = ", "
    greetString(2) = "world!\n"
    for (i <- 0 to 2)
      print(greetString(i))
  }

}

object C3 {
  def main(args: Array[String]): Unit  = {
    val c3 = new C3
    c3.cap_3_1_0()
  }
}
