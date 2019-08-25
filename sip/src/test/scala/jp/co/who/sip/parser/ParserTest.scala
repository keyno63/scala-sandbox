package jp.co.who.sip.parser

import wvlet.airspec._

class ParserTest extends AirSpec {

  def `sample code`(): Unit = {
    val parser = new Parser()
    parser.sample("sample") shouldBe "sample"

    parser.sample(10) shouldBe Some(10)
  }

  def `sample code2`(): Unit = {
    val parser = new Parser()
    parser.sample("get") shouldBe "get"

    parser.sample(-1) shouldBe Some(-1)
  }

}
