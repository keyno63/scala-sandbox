package com.github.keyno.sprayjson.domain

import com.github.keyno.sprayjson.domain.ExtendOther.FloatValue.{ BigDecimalNumber, DoubleNumber, FloatNumber }
import com.github.keyno.sprayjson.domain.ExtendOther.IntValue
import com.github.keyno.sprayjson.domain.ExtendOther.IntValue.{ BigIntNumber, IntNumber, LongNumber }
import org.scalatest.flatspec._
import spray.json.{ enrichAny, JsValue }

import scala.math.BigDecimal.exact

class OtherTest extends AnyFlatSpec {
  import com.github.keyno.sprayjson.domain.Other._

  "original other" should "parse json" in {
    {
      val other1: Other = StringValue("100")
      val ret1: JsValue = other1.toJson

      assert(ret1.toString() == "\"100\"")
      assert(ret1.convertTo[Other].toString == "StringValue(100)")

      val other2: Other = ObjectValue(Map("key1" -> StringValue("100")))
      val ret2: JsValue = other2.toJson
      assert(ret2.toString() == "{\"key1\":\"100\"}")
      assert(ret2.convertTo[Other].toString == "ObjectValue(Map(key1 -> StringValue(100)))")

      val other3: Other = ListValue(List(other1))
      val ret3: JsValue = other3.toJson
      assert(ret3.toString() == "[\"100\"]")
      assert(ret3.convertTo[Other].toString == "ListValue(List(StringValue(100)))")
    }
  }

  "json" should "parse other value" in {
    val value1: Other = IntValue(1)
    val json1         = value1.toJson
    val actual1       = json1.convertTo[Other]
    assert(actual1 == value1)

    val value2: BigDecimal = BigDecimal(Int.MaxValue) + 1
    val actual2            = value2.toJson.convertTo[Other]
    assert(actual2 == LongNumber(2147483648L))

    val value3: BigDecimal = BigDecimal(Long.MaxValue) + 1
    val actual3            = value3.toJson.convertTo[Other]
    assert(actual3 == BigIntNumber(BigDecimal("9223372036854775808").toBigInt()))

    val value12: BigDecimal = BigDecimal(0.1)
    val actual12            = value12.toJson.convertTo[Other]
    assert(actual12 == FloatNumber(0.1f))

    val value13: BigDecimal = BigDecimal(12345678901234.1)
    val actual13            = value13.toJson.convertTo[Other]
    assert(actual13 == DoubleNumber(12345678901234.1))

    val value14: BigDecimal = BigDecimal("1234567890123456.1")
    val actual14            = value14.toJson.convertTo[Other]
    assert(actual14 == BigDecimalNumber(value14))

    val y: BigDecimal = BigDecimal("10000000000000")

    def isExactFloat(value: BigDecimal) = {
      val f = value.toFloat
      !f.isInfinity && equals(exact(f.toDouble))
    }
  }
}
