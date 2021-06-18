package com.github.keyno.sprayjson.backend

import com.github.keyno.sprayjson.domain.{ GraphQLRequest, GraphQLResponse }
import com.github.keyno.sprayjson.interop.OrigError
import com.github.keyno.sprayjson.interop.OrigError.ExecutionError
import spray.json._

import scala.util.Try

class SprayJsonBackend {

  def parseHttpRequest(
    query: Option[String],
    op: Option[String],
    vars: Option[String],
    exts: Option[String]
  ): Either[Throwable, GraphQLRequest] = {
    // 第一案
    val varsJs = vars
      .flatMap(js =>
        Try(js.parseJson)
          .fold(v => Left(v), Right(_))
          .toOption
      )
    val extsJs = exts
      .flatMap(js =>
        Try(js.parseJson)
          .fold(v => Left(v), Right(_))
          .toOption
      )
    val opJs = op
      .flatMap(js =>
        Try(js.parseJson)
          .fold(v => Left(v), Right(_))
          .toOption
      )
    val queryJs = query
      .flatMap(js =>
        Try(js.parseJson)
          .fold(v => Left(v), Right(_))
          .toOption
      )
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
          Try(js.parseJson)
            .fold(v => Left(v), Right(_))
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
      .flatMap(js =>
        Try(js.convertTo[GraphQLRequest])
          .fold(r => Left(r), Right(_))
      )
  }

}
