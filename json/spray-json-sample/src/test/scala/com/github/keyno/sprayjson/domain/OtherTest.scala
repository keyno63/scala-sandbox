package com.github.keyno.sprayjson.domain

import org.scalatest.flatspec._
import spray.json.{ enrichAny, JsValue }

class OtherTest extends AnyFlatSpec {
  import com.github.keyno.sprayjson.domain.Other._

  "original other" should "xxx" in {
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
}
