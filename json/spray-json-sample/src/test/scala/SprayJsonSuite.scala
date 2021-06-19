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

  val valuez                         = parseJsonValue(targetJson)
  val valuex: Either[String, Person] = parseJsonValue(targetJson2)
  val tmp                            = valuez.right.get.toJson
  println("valuez: " + valuez)
  println(valuex)

}

case class Person(name: String, age: Int)

object MyJsonProtocol extends DefaultJsonProtocol {
  //implicit val personJsFmt = jsonFormat(Person.apply, "name_string", "age_int")
  implicit val personJsFmt: RootJsonFormat[Person] = jsonFormat2(Person.apply)
}
