package jp.co.who.sip.parser

import wvlet.airspec._

class ParserTest extends AirSpec {

  def `sample code`(): Unit = {
    val parser = new Parser()
    parser.sample("sample") shouldBe "sample"

    parser.sample(10) shouldBe Some(10)
  }

}
