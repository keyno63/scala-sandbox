package com.github.keyno.sprayjson.domain

import com.github.keyno.sprayjson.domain.ExtendOther.FloatValue.{ BigDecimalNumber, DoubleNumber, FloatNumber }
import com.github.keyno.sprayjson.domain.ExtendOther.IntValue.{ BigIntNumber, IntNumber, LongNumber }
import com.github.keyno.sprayjson.domain.ExtendOther.NullValue
import spray.json.{
  enrichAny,
  DefaultJsonProtocol,
  JsArray,
  JsNull,
  JsNumber,
  JsObject,
  JsString,
  JsValue,
  RootJsonFormat
}
import com.github.keyno.sprayjson.interop.json.ExtraSprayJson.{ extraSprayJsonRead, extraSprayJsonWrite }

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
      case value: ExtendOther => extraSprayJsonWrite(value)
      case ListValue(v)       => v.toJson(listFormat(sprayJsonOtherFormat))
      case ObjectValue(v)     => v.toJson(mapFormat[String, Other](StringJsonFormat, sprayJsonOtherFormat))
//      case list @ ListValue(_) => v1.write(list)
//      case ov @ ObjectValue(_) => v2.write(ov)
      case StringValue(value) => JsString(value)
    }

    override def read(json: JsValue): Other = json match {
      case JsNull => NullValue
      case JsNumber(v) =>
        if (v.isWhole) {
          if (v.isValidInt) IntNumber(v.toInt)
          else if (v.isValidLong) LongNumber(v.toLong)
          else BigIntNumber(v.toBigInt())
        } else {
          if (v.isDecimalFloat) FloatNumber(v.toFloat)
          else if (v.isDecimalDouble) DoubleNumber(v.toDouble)
          else BigDecimalNumber(v)
        }
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

sealed trait ExtendOther extends Other
object ExtendOther {
  case object NullValue                   extends ExtendOther
  sealed trait IntValue                   extends ExtendOther
  sealed trait FloatValue                 extends ExtendOther
  case class StringValue2(value: String)  extends ExtendOther
  case class BooleanValue(value: Boolean) extends ExtendOther
  case class EnumValue(value: String)     extends ExtendOther

  object IntValue {
    def apply(v: Int): IntValue    = IntNumber(v)
    def apply(v: Long): IntValue   = LongNumber(v)
    def apply(v: BigInt): IntValue = BigIntNumber(v)
    case class IntNumber(value: Int)       extends IntValue
    case class LongNumber(value: Long)     extends IntValue
    case class BigIntNumber(value: BigInt) extends IntValue
  }

  object FloatValue {
    def apply(v: Float): FloatValue      = FloatNumber(v)
    def apply(v: Double): FloatValue     = DoubleNumber(v)
    def apply(v: BigDecimal): FloatValue = BigDecimalNumber(v)
    case class FloatNumber(value: Float)           extends FloatValue
    case class DoubleNumber(value: Double)         extends FloatValue
    case class BigDecimalNumber(value: BigDecimal) extends FloatValue
  }
}
