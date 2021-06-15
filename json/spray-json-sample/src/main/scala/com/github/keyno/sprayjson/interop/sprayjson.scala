package com.github.keyno.sprayjson.interop

import com.github.keyno.sprayjson.domain.ExtendOther.FloatValue.{ BigDecimalNumber, DoubleNumber, FloatNumber }
import com.github.keyno.sprayjson.domain.ExtendOther.IntValue.{ BigIntNumber, IntNumber, LongNumber }
import com.github.keyno.sprayjson.domain.ExtendOther._
import com.github.keyno.sprayjson.domain.Other.{ sprayJsonOtherFormat, ListValue, ObjectValue, StringValue }
import com.github.keyno.sprayjson.domain.{ ExtendOther, Other }
import spray.json.{ DefaultJsonProtocol, JsArray, JsBoolean, JsNull, JsNumber, JsObject, JsString, JsValue }

object json {
  private[com] object ExtraSprayJson extends DefaultJsonProtocol {
    // DefaultJsonProtocol は StringJsonFormat などを呼ぶため

    // Encoder for Json
    def extraSprayJsonWrite(obj: ExtendOther): JsValue =
      obj match {
        case NullValue => JsNull
        case v: IntValue =>
          v match {
            case IntNumber(x)    => JsNumber(x)
            case LongNumber(x)   => JsNumber(x)
            case BigIntNumber(x) => JsNumber(x)
          }
        case v: FloatValue =>
          v match {
            case FloatNumber(x)      => JsNumber(x)
            case DoubleNumber(x)     => JsNumber(x)
            case BigDecimalNumber(x) => JsNumber(x)
          }
        case StringValue2(value) => JsString(value)
        case BooleanValue(value) => JsBoolean(value)
        case EnumValue(value)    => JsString(value)
      }

    // Encoder for Json
    def extraSprayJsonRead(json: JsValue): Other = json match {
      // 追加
      case JsNull => NullValue
      case JsNumber(v) =>
        if (v.isWhole) {
          if (v.isValidInt) IntNumber(v.toInt)
          else if (v.isValidLong) LongNumber(v.toLong)
//          else if (v.isDecimalFloat) FloatNumber(v.toFloat)
//          else if (v.isDecimalDouble) DoubleNumber(v.toDouble)
          else BigIntNumber(v.toBigInt())
        } else {
          if (v.isDecimalFloat) FloatNumber(v.toFloat)
          else if (v.isDecimalDouble) DoubleNumber(v.toDouble)
          else BigDecimalNumber(v)
        }
      // 既存
      case JsString(value) => StringValue(value)
      case JsObject(_) => {
        val mapValue = mapFormat[String, Other](StringJsonFormat, sprayJsonOtherFormat).read(json)
        ObjectValue(mapValue)
      }
      case JsArray(_) => {
        ListValue(listFormat[Other](sprayJsonOtherFormat).read(json))
      }
      case _ => throw new Exception("not implimented now")
    }

  }
}
