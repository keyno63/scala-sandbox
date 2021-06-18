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

    override def write(obj: Other): JsValue = extraSprayJsonWrite(obj)

    override def read(json: JsValue): Other = extraSprayJsonRead(json)
  }
}

sealed trait ExtendOther extends Other with ResponseValue
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

sealed trait ResponseValue
object ResponseValue {
  case class ListValue(value: List[ResponseValue])              extends ResponseValue
  case class ObjectValue(fields: List[(String, ResponseValue)]) extends ResponseValue
  // skip implemented by using zio.Stream.
  //case class StreamValue()
}
