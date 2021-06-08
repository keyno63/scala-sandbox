package com.github.keyno.sprayjson.domain

import spray.json.{ enrichAny, JsObject, JsString, JsValue, RootJsonFormat }

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
      // 以下は同じだった
//      Map(
//        "query"      -> JsString(obj.query.getOrElse("")),
//        "operation"  -> JsString(obj.operationName.getOrElse("")),
//        "variable"   -> obj.variable.getOrElse(Map.empty).toJson,
//        "extensions" -> obj.extensions.getOrElse(Map.empty).toJson
//      ).toJson
      JsObject(
        Map(
          "query"      -> JsString(obj.query.getOrElse("")),
          "operation"  -> JsString(obj.operationName.getOrElse("")),
          "variable"   -> obj.variable.getOrElse(Map.empty).toJson,
          "extensions" -> obj.extensions.getOrElse(Map.empty).toJson
        )
      )

    override def read(json: JsValue): GraphQLRequest =
      json match {
        case JsObject(m) => {
          // Map の変換はできたが、そもそも Map をいい感じに case class に変換する方法もおもいつかないので、いまのままでいいかも
          val convertedMap = m.map {
            case (k, v) =>
              (k, k match {
                case "query" | "operation"     => v.convertTo[String]
                case "variable" | "extensions" => v.convertTo[Map[String, Other]]
              })
          }
          GraphQLRequest(
            convertedMap.get("query").asInstanceOf[Option[String]],
            convertedMap.get("operation").asInstanceOf[Option[String]],
            convertedMap.get("variable").asInstanceOf[Option[Map[String, Other]]],
            convertedMap.get("extensions").asInstanceOf[Option[Map[String, Other]]]
          )

          // 本来の実装
          val query: Option[String]                  = m.get("query").map(_.convertTo[String])
          val operation: Option[String]              = m.get("operation").map(_.convertTo[String])
          val variable: Option[Map[String, Other]]   = m.get("variable").map(_.convertTo[Map[String, Other]])
          val extensions: Option[Map[String, Other]] = m.get("extensions").map(_.convertTo[Map[String, Other]])

          GraphQLRequest(
            query,
            operation,
            variable,
            extensions
          )
        }
        case v @ _ => throw new Exception(s"parseError $v")
      }
  }
}
