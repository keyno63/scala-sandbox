package com.github.keyno.sprayjson.domain

import spray.json.{ enrichAny, JsArray, JsString, JsValue, RootJsonFormat }

case class GraphQLRequest(
  query: Option[String] = None,
  operationName: Option[String] = None,
  variable: Option[Map[String, Other]] = None,
  extensions: Option[Map[String, Other]] = None
)

object GraphQLRequest {
  import com.github.keyno.sprayjson.domain.Other._

  implicit object sprayJsonFormat extends RootJsonFormat[GraphQLRequest] {
    override def write(obj: GraphQLRequest): JsValue =
      JsArray(
        JsString(obj.query.getOrElse("")),
        JsString(obj.operationName.getOrElse("")),
        obj.variable.getOrElse(Map.empty).toJson,
        obj.extensions.getOrElse(Map.empty).toJson
      )

    override def read(json: JsValue): GraphQLRequest = json.convertTo[GraphQLRequest](sprayJsonFormat)
  }
}
