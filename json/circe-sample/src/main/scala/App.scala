import io.circe
import io.circe.parser._
import io.circe.generic.auto._

object CirceApp extends scala.App {

  val obj = new CirceParsing
  val jsonString = """{"id": 10, "name": "John"}"""
  println(obj.parseString(jsonString))

  val optionStr = Some(jsonString)
  println(obj.parseString(optionStr))
  val noneStr = None
  println(obj.parseString(noneStr))

}

case class User(id: Int, name: String)

class CirceParsing {
  def parseString(string: String): Either[circe.Error, User] = {
    parse(string).flatMap(_.as[User])
  }

  def parseString(stringOption: Option[String]): Either[circe.Error, User] = {
    parse(stringOption.getOrElse("")).flatMap(_.as[User])
  }
}