package jp.co.who.cop.c6

import wvlet.airspec.AirSpec

class RationalSpec extends AirSpec {

  def `constructor test`(): Unit = {
    val a = new Rational(3, 4)
    a.toString shouldBe "3/4"

    val b = new Rational(2, 4)
    b.toString shouldBe "1/2"

    /*
    val c = new Rational(2, 0)
    c.toString shouldBe "2"
    */
  }

  def `table test`(): Unit = {
    val z = Seq(
      (3, 4),
      (2, 4),
      //(2, 0),
    )

    val expect = Seq(
      "3/4", "1/2", //"2"
    )

    var i = 0
    while (i < z.length) {
      val a = new Rational(z(i)._1, z(i)._2)
      a.toString shouldBe expect(i)
      i += 1
    }
  }

}

