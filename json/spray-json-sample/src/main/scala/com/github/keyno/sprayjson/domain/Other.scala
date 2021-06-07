package com.github.keyno.sprayjson.domain

sealed trait Other
object Other {
  case class ListValue(value: List[Other])          extends Other
  case class ObjectValue(value: Map[String, Other]) extends Other
  case class StringValue(value: String)             extends Other
}
