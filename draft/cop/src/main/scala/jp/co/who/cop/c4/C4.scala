package jp.co.who.cop.c4

class C4 {
  class ChecksumAccumulator {
    var sum                = 0
    def add(b: Byte): Unit = sum += b
    def uncheck(): Int     = ~(sum & 0xFF) + 1
  }

  def cap_4_1_0(): Unit = {
    val acc = new ChecksumAccumulator
    val csa = new ChecksumAccumulator
    println(acc.sum, csa.sum)
    acc.sum = 3
    println(acc.sum, csa.sum)
  }

  import scala.collection.mutable
  object ChecksumAccumulator {
    private val cache = mutable.Map.empty[String, Int]
    def calculate(s: String): Int =
      if (cache.contains(s))
        cache(s)
      else {
        val acc = new ChecksumAccumulator
        for (c <- s)
          acc.add(c.toByte)
        val cs = acc.uncheck()
        cache += (s -> cs)
        cs
      }
  }
  def cap_4_3_0(): Unit = {
    val s   = "Every value is an object"
    val ret = ChecksumAccumulator.calculate(s)
    println(ret)
  }

}

object C4 extends App {
  val c4 = new C4
  c4.cap_4_1_0()
  c4.cap_4_3_0()
}
