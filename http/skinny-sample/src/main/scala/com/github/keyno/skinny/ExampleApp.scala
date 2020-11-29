package com.github.keyno.skinny

import skinny.http.{ HTTP, Request }
import com.typesafe.config._

object ExampleApp extends scala.App {

  // setting config.
  val config            = ConfigFactory.load()
  val url               = config.getString("app.url")
  val connectionTimeout = config.getInt("app.connectionTimeOut")
  val readTimeout       = config.getInt("app.readTimeOut")

  // 自前の mock server 用のリクエスト先.
  val request = new Request(url)
  request.connectTimeoutMillis(connectionTimeout)
  request.readTimeoutMillis(readTimeout)

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
