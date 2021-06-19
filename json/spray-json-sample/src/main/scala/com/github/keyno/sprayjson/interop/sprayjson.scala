package com.github.keyno.sprayjson.interop

import com.github.keyno.sprayjson.domain.ExtendOther.FloatValue.{ BigDecimalNumber, DoubleNumber, FloatNumber }
import com.github.keyno.sprayjson.domain.ExtendOther.IntValue.{ BigIntNumber, IntNumber, LongNumber }
import com.github.keyno.sprayjson.domain.ExtendOther._
import com.github.keyno.sprayjson.domain.Other.{ sprayJsonOtherFormat, ListValue, ObjectValue, StringValue }
import com.github.keyno.sprayjson.domain.{ ExtendOther, GraphQLRequest, GraphQLResponse, Other, ResponseValue }
import spray.json.{
  enrichAny,
  DefaultJsonProtocol,
  JsArray,
  JsBoolean,
  JsNull,
  JsNumber,
  JsObject,
  JsString,
  JsValue,
  RootJsonFormat
}

object json {
  private[com] object ExtraSprayJson extends DefaultJsonProtocol {
    // DefaultJsonProtocol は StringJsonFormat などを呼ぶため

    // Encoder for Json
    def extraSprayJsonWrite(obj: Other): JsValue = obj match {
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
      case StringValue2(value)       => JsString(value)
      case BooleanValue(value)       => JsBoolean(value)
      case EnumValue(value)          => JsString(value)
      case ListValue(v: List[Other]) =>
        //v.toJson(listFormat(sprayJsonOtherFormat))
        v.toJson
      case ObjectValue(v)     => v.toJson(mapFormat[String, Other](StringJsonFormat, sprayJsonOtherFormat))
      case StringValue(value) => JsString(value)
    }

    // Encoder for Json
    def extraSprayJsonRead(json: JsValue): Other = json match {
      // 追加
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

    // ResponseValue Encoder/Decoder
    // ResponseValue Encoder
    def write(obj: ResponseValue): JsValue = obj match {
      case value: Other => extraSprayJsonWrite(value)
      case ResponseValue.ListValue(v) => //v.toJson(listFormat(sprayJsonOtherFormat))
        JsArray(v.map(write).toVector)
      case ResponseValue.ObjectValue(f) => JsObject(f.map { case (k, v) => k -> write(v) }.toMap)
    }

    // ResponseValue Decoder
    def read(json: JsValue): ResponseValue = json match {
      case JsNull => NullValue
      case JsNumber(v) => {
        val opsNum = Some(v)
        opsNum.map(_.toBigInt).map(IntValue.apply) orElse
          opsNum.map(FloatValue.apply) getOrElse
          FloatValue(v.toDouble)

      }
      case JsString(s) => StringValue2(s)
      case JsArray(ar) => ResponseValue.ListValue(ar.toList.map(read))
      case JsObject(o) => ResponseValue.ObjectValue(o.toList.map { case (k, v) => k -> read(v) })
      case _           => throw new Exception("undefined")
    }

    private def convertToJsNumber(v: Other): JsNumber = v match {
      case IntNumber(x)        => JsNumber(x)
      case LongNumber(x)       => JsNumber(x)
      case BigIntNumber(x)     => JsNumber(x)
      case FloatNumber(x)      => JsNumber(x)
      case DoubleNumber(x)     => JsNumber(x)
      case BigDecimalNumber(x) => JsNumber(x)
      case _                   => throw new Exception("not js number")
      //      case v: IntValue => {
//        case IntNumber(x) => JsNumber(x)
//        case LongNumber(x) => JsNumber(x)
//        case BigIntNumber(x) => JsNumber(x)
//      }
//      case v: FloatValue =>
//        v match {
//          case FloatNumber(x) => JsNumber(x)
//          case DoubleNumber(x) => JsNumber(x)
//          case BigDecimalNumber(x) => JsNumber(x)
//        }
    }

    private def convertJsNumberToOther(jsNumber: JsNumber): ExtendOther = {
      val v = jsNumber.value
      if (v.isWhole) {
        if (v.isValidInt) IntNumber(v.toInt)
        else if (v.isValidLong) LongNumber(v.toLong)
        else BigIntNumber(v.toBigInt())
      } else {
        if (v.isDecimalFloat) FloatNumber(v.toFloat)
        else if (v.isDecimalDouble) DoubleNumber(v.toDouble)
        else BigDecimalNumber(v)
      }
    }
  }

  private[com] object GraphQLResponseSprayJson {
    def write(obj: GraphQLResponse[Any]): JsValue = obj match {
      case GraphQLResponse(data, Nil, None) => JsObject("data" -> ExtraSprayJson.write(data))
      case GraphQLResponse(data, Nil, Some(extensions)) =>
        JsObject(
          "data"       -> ExtraSprayJson.write(data),
          "extensions" -> ExtraSprayJson.write(extensions.asInstanceOf[ResponseValue])
        )
      case GraphQLResponse(data, errors, None) =>
        JsObject("data" -> ExtraSprayJson.write(data), "errors" -> JsArray(errors.map(handleError).toVector))
      case GraphQLResponse(data, errors, Some(extensions)) =>
        JsObject(
          "data"       -> ExtraSprayJson.write(data),
          "errors"     -> JsArray(errors.map(handleError).toVector),
          "extensions" -> ExtraSprayJson.write(extensions.asInstanceOf[ResponseValue])
        )
    }
    def read(json: JsValue): GraphQLResponse[OrigError] = json match {
      case JsObject(m) => {
        val a: Option[ResponseValue] = m.get("data").map(ExtraSprayJson.read)
        //val b                        = m.get("errors").map(_.convertTo[List[OrigError]])
        val errors = m.get("errors").map {
          case JsArray(vv) => vv.map(ErrorSprayJson.read).toList
//          case _ => {
//            println(m.get("errors"))
//            None
//          }
          case _ => throw new Exception("maybe not implemented now")
        }
        GraphQLResponse[OrigError](a.get, errors.getOrElse(List()), None)
      }
      case _ => throw new Exception("maybe not implemented now")
    }

    private def handleError(err: Any): JsValue =
      err match {
        case _ => JsObject("message" -> JsString(err.toString))
      }
  }

  private[com] object ErrorSprayJson extends RootJsonFormat[OrigError] {
    import spray.json.DefaultJsonProtocol._

    def write(obj: OrigError): JsValue = obj match {
      case _ => JsObject("message" -> JsString(obj.getMessage))
    }

    override def read(json: JsValue): OrigError = json match {
      case JsObject(m) => {
//        for {
//          aa <- m.get("message").map(_.convertTo[String])
//        } yield OrigError.ExecutionError(aa)
        val message = m.get("message").map(_.convertTo[String])
        OrigError.ExecutionError(message.getOrElse(""))
      }
      case _ => throw new Exception("not implemented now")
    }
  }
}

sealed trait OrigError extends Throwable with Product with Serializable {
  def msg: String
  override def getMessage: String = msg
}
object OrigError {
  case class ExecutionError(msg: String) extends OrigError {
    override def toString = msg
  }
}
