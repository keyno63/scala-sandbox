package jp.co.who.cop.c16

import wvlet.airspec.AirSpec

class ControllListSpec extends AirSpec {

  def `isort`(): Unit = {
    ControllList.isort(List(10, 3, 2, 1)) shouldBe List(1, 2, 3, 10)

    ControllList.isort(List(1, 2, 3, 10)) shouldBe List(1, 2, 3, 10)

    ControllList.isort(List(10, 1, 2, 3)) shouldBe List(1, 2, 3, 10)
  }

}
