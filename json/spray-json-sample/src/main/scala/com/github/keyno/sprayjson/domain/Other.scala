package com.github.keyno.sprayjson.domain

import spray.json.{ enrichAny, DefaultJsonProtocol, JsArray, JsObject, JsString, JsValue, RootJsonFormat }

sealed trait Other
object Other extends DefaultJsonProtocol {
  case class ListValue(value: List[Other]) extends Other

  case class ObjectValue(value: Map[String, Other]) extends Other

  case class StringValue(value: String) extends Other

//  implicit val v1: RootJsonFormat[ListValue]   = jsonFormat1(ListValue.apply)
//  implicit val v2: RootJsonFormat[ObjectValue] = jsonFormat1(ObjectValue.apply)
  //implicit val v3 = jsonFormat1(ListValue.apply)
  implicit object sprayJsonOtherFormat extends RootJsonFormat[Other] {
    override def write(obj: Other): JsValue = obj match {
      case ListValue(v)   => v.toJson(listFormat(sprayJsonOtherFormat))
      case ObjectValue(v) => v.toJson(mapFormat[String, Other](StringJsonFormat, sprayJsonOtherFormat))
//      case list @ ListValue(_) => v1.write(list)
//      case ov @ ObjectValue(_) => v2.write(ov)
      case StringValue(value) => JsString(value)
    }

    override def read(json: JsValue): Other = json match {
      case JsString(value) => StringValue(value)
      case JsObject(_) => {
        val mapValue = mapFormat[String, Other](StringJsonFormat, sprayJsonOtherFormat).read(json)
        ObjectValue(mapValue)
      }
      case JsArray(_) => {
        ListValue(listFormat[Other](sprayJsonOtherFormat).read(json))
      }
    }
  }
}
