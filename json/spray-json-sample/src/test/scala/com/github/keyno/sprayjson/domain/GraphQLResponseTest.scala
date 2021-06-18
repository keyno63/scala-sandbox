package com.github.keyno.sprayjson.domain

import com.github.keyno.sprayjson.domain.ExtendOther.NullValue
import com.github.keyno.sprayjson.interop.OrigError
import com.github.keyno.sprayjson.interop.OrigError.ExecutionError
import com.github.keyno.sprayjson.interop.json.GraphQLResponseSprayJson
import org.scalatest.wordspec.AnyWordSpec
import spray.json.{ enrichAny, JsArray, JsNull, JsObject, JsString, JsValue, RootJsonFormat }

class GraphQLResponseTest extends AnyWordSpec {

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
  "graphql response" should {
    "be able to parsed" in {
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

    "be able to convertTo" in {
      val error = ExecutionError("second error")
      val expected = GraphQLResponse(
        ResponseValue.ListValue(List(NullValue)),
        List(error)
      )

      val json1 = JsObject(
        Map(
          "data" -> JsArray(JsNull),
          "errors" -> JsArray(
            JsObject("message" -> JsString("second error"))
          )
        )
      )

      val actual = json1.convertTo[GraphQLResponse[Any]]
      assert(actual == expected)

    }
  }
}
