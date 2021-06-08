package com.github.keyno.sprayjson.domain

import com.github.keyno.sprayjson.domain.Other._
import org.scalatest.flatspec.AnyFlatSpec
import spray.json.{ enrichAny, JsArray, JsString, JsValue }

class GraphQLRequestTest extends AnyFlatSpec {
  import com.github.keyno.sprayjson.domain.GraphQLRequest._

  "graphql request class" should "convert to JsonObj and JsonString" in {
    val gql1: GraphQLRequest = GraphQLRequest(
      query = Some("queryValue"),
      operationName = Some("operationValue"),
      variable = Some(Map("key1" -> StringValue("value1"))),
      extensions = Some(
        Map(
          "key2" ->
            ListValue(List(StringValue("lValue1"), StringValue("lValue2")))
        )
      )
    )
    val ret1: JsValue = gql1.toJson
//    val expected1: JsValue = JsArray(
//      JsString("queryValue"),
//      JsString("operationValue"),
//      Map("key1" -> StringValue("value1")).toJson(mapFormat(StringJsonFormat, sprayJsonOtherFormat)),
//      Map(
//        "key2" ->
//          ListValue(List(StringValue("lValue1"), StringValue("lValue2")))
//      ).toJson(mapFormat(StringJsonFormat, sprayJsonOtherFormat))
//    )
    // なにか間違っている気がする. Object 形式になっていないのではないか
    val expected1: String = """["queryValue","operationValue",{"key1":"value1"},{"key2":["lValue1","lValue2"]}]"""
    assert(ret1.toString() == expected1)
  }

}
