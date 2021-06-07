import org.scalatest.flatspec._
import spray.json._

import scala.util.Try

class SprayJsonSuite extends AnyFlatSpec {

  import MyJsonProtocol._

  val person: Person = Person("aaa", 100)
  person.toJson

  val targetJson: String =
    """
      |{
      |  "age": 24,
      |  "name": "Ed"
      |}
      |""".stripMargin

  // == to JsValue ==
  // case class => JsValue
  val jsonPerson: JsValue = person.toJson
  // String => JsValue
  val json: JsValue = targetJson.parseJson

  // == to case class ==
  // JsValue => case class
  val case1: Person = jsonPerson.convertTo[Person]
  //convertTo[Person]

  val targetJson2: String =
    """
      |{
      |  "name_string": "Ed",
      |  "age_int": 24
      |}
      |""".stripMargin

  def parseJsonValue(value: String) =
    Try(value.parseJson.convertTo[Person])
      .fold(throwValue => Left(s"failed to parse ${throwValue.toString}"), Right(_))

  val valuez = parseJsonValue(targetJson)
  val valuex = parseJsonValue(targetJson2)
  println("valuez: " + valuez)
  println(valuex)

}

case class Person(name: String, age: Int)

object MyJsonProtocol extends DefaultJsonProtocol {
  //implicit val personJsFmt = jsonFormat(Person.apply, "name_string", "age_int")
  implicit val personJsFmt: RootJsonFormat[Person] = jsonFormat2(Person.apply)
}

/**
 * - 流れ
 *   - object に DefaultJsonProtocol を継承する
 *   - JsonFormatter を定義する
 *     - in the case of case class,
 *       implicit val, from jsonFormatN
 *     - in the case of class,
 *       function write and read, in implicit object
 *       from JsArray(...) and logic to convert to class.
 *   - 実装する class/object で JsonFormatter を import する
 *   - Json への encode, decode を行い、JsValue を得る
 * - できること
 *   - string => JsValue (.parseJson)
 *   - case class => JsValue (.toJson with implicit JsonFormatter)
 *   - JsValue => case class (.convertTo[case class] with JsonFormatter)
 *   - class => JsValue (.toJson)
 *   - JsValue => case class (.convertTo[class] with )
 */
