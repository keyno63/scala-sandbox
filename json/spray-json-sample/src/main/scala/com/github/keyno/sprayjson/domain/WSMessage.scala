package com.github.keyno.sprayjson.domain

import spray.json.JsValue

import scala.util.Try

trait WSMessage {
  def id: String
  def messageType: String
  def request: Option[GraphQLRequest]
}

case class SprayJsonWSMessage(id: String, messageType: String, payload: Option[JsValue]) extends WSMessage {
  lazy val request: Option[GraphQLRequest] = payload.flatMap(vv => Try(vv.convertTo[GraphQLRequest]).toOption)
}
