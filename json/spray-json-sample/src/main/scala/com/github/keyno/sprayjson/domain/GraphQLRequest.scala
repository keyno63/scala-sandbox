package com.github.keyno.sprayjson.domain

case class GraphQLRequest(
  query: Option[String] = None,
  operationName: Option[String] = None,
  variable: Option[Map[String, Other]] = None,
  extensions: Option[Map[String, Other]] = None
)
