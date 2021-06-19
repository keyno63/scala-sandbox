package com.github.keyno.sprayjson.backend

import akka.http.scaladsl.unmarshalling.FromEntityUnmarshaller
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.github.keyno.sprayjson.domain.GraphQLRequest.sprayJsonFormat
import com.github.keyno.sprayjson.domain.{
  GraphQLRequest,
  GraphQLResponse,
  ResponseValue,
  SprayJsonWSMessage,
  WSMessage
}
import com.github.keyno.sprayjson.interop.OrigError
import com.github.keyno.sprayjson.interop.OrigError.ExecutionError
import com.github.keyno.sprayjson.interop.json.GraphQLResponseSprayJson
import spray.json._

import scala.util.Try

class SprayJsonBackend extends SprayJsonSupport {

  def parseHttpRequest(
    query: Option[String],
    op: Option[String],
    vars: Option[String],
    exts: Option[String]
  ): Either[Throwable, GraphQLRequest] = {
    // 第一案
    val varsJs = vars
      .flatMap(js => Try(js.parseJson).toOption)
    val extsJs = exts
      .flatMap(js => Try(js.parseJson).toOption)
    val opJs = op
      .flatMap(js => Try(js.parseJson).toOption)
    val queryJs = query
      .flatMap(js => Try(js.parseJson).toOption)
    val jsObj = for {
      qjs <- queryJs
      ojs <- opJs
      vjs <- varsJs
      ejs <- extsJs
    } yield JsObject(
      "query"         -> qjs,
      "operationName" -> ojs,
      "variables"     -> vjs,
      "extensions"    -> ejs
    )
    //jsObj.map(_.convertTo[GraphQLRequest]) flatMap Right

    // 第二案
    def convert(v: Option[String]): JsValue =
      v.flatMap(js =>
          Try(js.parseJson).toEither // ここどうする？
          .toOption
        )
        .get

    // query, op は parse 不要？
    // vars, exts は JsValue にする必要がある
    val jsObj2 = for {
      qjs <- query
      ojs <- op
    } yield JsObject(
      "query"         -> JsString(qjs),
      "operationName" -> JsString(ojs),
      "variables"     -> convert(vars),
      "extensions"    -> convert(exts)
    )

    jsObj2
      .toRight(ExecutionError("failed to parse, query and operationName is required"))
      .flatMap(js => Try(js.convertTo[GraphQLRequest]).toEither)
  }

  // TODO: impl in production code
  implicit object parseImpl extends RootJsonFormat[GraphQLResponse[Any]] {
    override def write(obj: GraphQLResponse[Any]): JsValue       = GraphQLResponseSprayJson.write(obj)
    override def read(json: JsValue): GraphQLResponse[OrigError] = GraphQLResponseSprayJson.read(json)
  }

  def encodeGraphQLResponse(r: GraphQLResponse[Any]): String =
    r.toJson.toString()

  import spray.json.DefaultJsonProtocol._ // to import StringJsonFormat
  def parseWSMessage(text: String): Either[Throwable, WSMessage] =
    Try {
      val json = text.parseJson
      json match {
        case JsObject(m) => {
          SprayJsonWSMessage(
            m.get("id").map(_.convertTo[String]).getOrElse(""),
            m.get("type").map(_.convertTo[String]).getOrElse(""),
            m.get("payload")
          )
        }
      }
    }.toEither

  def encodeWSResponse[E](id: String, data: ResponseValue, errors: List[E]): String =
    JsObject(
      "id"   -> JsString(id),
      "type" -> JsString("data"),
      //"payload" -> GraphQLResponse(data, errors).toJson(parseImpl),
      "payload" -> GraphQLResponseSprayJson.write(GraphQLResponse(data, errors))
    ).prettyPrint // or compactPrint?

  def encodeWSError(id: String, error: String): String =
    JsObject(
      "id"      -> JsString(id),
      "type"    -> JsString("data"),
      "payload" -> JsObject("message" -> JsString(error))
    ).prettyPrint // or compactPrint?

  def reqUnmarshaller: FromEntityUnmarshaller[GraphQLRequest] = implicitly
}
