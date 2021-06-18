package com.github.keyno.sprayjson.domain

import com.github.keyno.sprayjson.domain.ExtendOther.NullValue
import com.github.keyno.sprayjson.interop.OrigError
import com.github.keyno.sprayjson.interop.OrigError.ExecutionError
import com.github.keyno.sprayjson.interop.json.GraphQLResponseSprayJson
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.convertToAnyShouldWrapper
import spray.json.{ enrichAny, JsArray, JsNull, JsObject, JsString, JsValue, RootJsonFormat }

class GraphQLResponseTest extends /*AnyFunSuite*/ AnyFlatSpec {

  // TODO: impl in production code
  implicit object parseImpl extends RootJsonFormat[GraphQLResponse[Any]] {
    override def write(obj: GraphQLResponse[Any]): JsValue       = GraphQLResponseSprayJson.write(obj)
    override def read(json: JsValue): GraphQLResponse[OrigError] = GraphQLResponseSprayJson.read(json)
  }
//  "a" should {
//    "x" in {
//      assert(true)
//    }
//  }
  "graphql response" should "be parsed" in {
    val error                             = ExecutionError("first error")
    val gqlResponse: GraphQLResponse[Any] = GraphQLResponse(ResponseValue.ListValue(List(NullValue)), List(error))

    val json1 = gqlResponse.toJson
    val expected1 = JsObject(
      Map(
        "data" -> JsArray(JsNull),
        "errors" -> JsArray(
          JsObject("message" -> JsString("first error"))
        )
      )
    )
    assert(json1 == expected1)

    val ret = json1.convertTo[GraphQLResponse[Any]]
    assert(ret == gqlResponse)
  }
}
