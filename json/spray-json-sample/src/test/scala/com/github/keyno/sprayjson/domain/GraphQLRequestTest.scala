package com.github.keyno.sprayjson.domain

import com.github.keyno.sprayjson.domain.Other._
import org.scalatest.flatspec.AnyFlatSpec
import spray.json.{ enrichAny, enrichString, JsString, JsValue }

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
    val expected0: JsValue = Map(
      "query"     -> JsString("queryValue"),
      "operation" -> JsString("operationValue"),
      "variable"  -> Map("key1" -> "value1").toJson,
      "extensions" ->
        Map(
          "key2" ->
            List("lValue1", "lValue2")
        ).toJson
    ).toJson
    assert(ret1 == expected0)

    val expected1: String =
      """{"query":"queryValue","operation":"operationValue","variable":{"key1":"value1"},"extensions":{"key2":["lValue1","lValue2"]}}"""
    assert(ret1.toString() == expected1)
  }
  "graphql JsObject" should "convert to Graphql case class" in {

    val expected = GraphQLRequest(
      Some("queryValue"),
      Some("operationValue"),
      Some(Map("key1" -> StringValue("value1"))),
      Some(Map("key2" -> ListValue(List(StringValue("lValue1"), StringValue("lValue2")))))
    )

    val jsonStr: String =
      """{"query":"queryValue","operation":"operationValue","variable":{"key1":"value1"},"extensions":{"key2":["lValue1","lValue2"]}}"""
    val jsonObj0: JsValue = jsonStr.parseJson
    println(jsonObj0)
    val gql0: GraphQLRequest = jsonObj0.convertTo[GraphQLRequest]
    assert(gql0 == expected)

    val jsonObj: JsValue = Map(
      "query"     -> JsString("queryValue"),
      "operation" -> JsString("operationValue"),
      "variable"  -> Map("key1" -> "value1").toJson,
      "extensions" ->
        Map(
          "key2" ->
            List("lValue1", "lValue2")
        ).toJson
    ).toJson

    val gql: GraphQLRequest = jsonObj.convertTo[GraphQLRequest]
    assert(gql == expected)
  }

}
