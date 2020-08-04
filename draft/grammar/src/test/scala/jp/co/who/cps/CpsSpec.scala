package jp.co.who.cps

import wvlet.airspec._

class CpsSpec extends AirSpec {

  def `sample code`(): Unit = {
    val testData = Seq(
      "100-1000",
      "163-0022",
      "640-1192"
    )

    testData.foreach(value => Cps.f(Something(value)) shouldBe value)
  }
}
