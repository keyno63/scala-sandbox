package com.github.keyno.skinny

import skinny.http.{ HTTP, Request }

object ExampleApp extends scala.App {

  // 自前の mock server 用のリクエスト先.
  val url     = "http://localhost:8080/api/webclient/test3"
  val request = new Request(url)
  request.connectTimeoutMillis(5000)
  request.readTimeoutMillis(5000)

  // 自前の mock server 用の body.
  val body =
    """
      |{
      |  "v": "value",
      |  "c": [
      |    {
      |      "key1": "value_1",
      |      "key2": "value_2"
      |    }
      |  ]
      |}
      |""".stripMargin

  request.userAgent = None
  if (body.isEmpty) request.bodyBytes = None
  else request.body(body.getBytes, "application/json")

  val response: skinny.http.Response = HTTP.post(request)

  val statusCode: Int = response.status
  val httpStatus: String = statusCode match {
    case 200 => "OK"
    case 201 => "CREATED"
    case 204 => "NO_CONTENT"
    case 401 => "UNAUTHORIZED"
    case 400 => "BAD_REQUEST"
    case 404 => "NOT_FOUND"
    case 500 => "INTERNAL_SERVER_ERROR"
    case _   => "OTHER_STATUS_CODE"
  }
  val bodyValue: String = response.textBody
  print(Response(statusCode, httpStatus, bodyValue))

  case class Response(statusCode: Int, status: String, body: String)
}
