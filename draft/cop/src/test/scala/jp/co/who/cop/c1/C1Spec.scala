package jp.co.who.cop.c1

import org.scalatest.{FunSuite, Matchers}

class C1Spec extends FunSuite with Matchers {

  val c1 = new C1

  test("capital の確認")
  {
    val expected = Map(
      "Map" -> "Washington",
      "France" -> "Paris",
      "Japan" -> "Tokyo"
    )

    c1.cap_1_1_0()
    c1.getCapital should equal (expected)
  }

  test("MyClass 生成の確認")
  {
    val expectedIndex = 1
    val expectedName = "name"

    val actual = c1.cap_1_3_2()
    actual.index should equal (expectedIndex)
    actual.name should equal (expectedName)
  }

  ignore("ignore test の確認")
  {
    assert(false)
  }

}

