import io.circe.{ Json, ParsingFailure }
// 自動で変換したい場合
//import io.circe.generic.auto._
// Encoder/Decoder を自作したい場合
import io.circe.{ Decoder, Encoder }
import io.circe.syntax._
import org.scalatest.flatspec._

class CirceParsingSuite extends AnyFlatSpec {

  val circeParcing = new CirceParsing

  "parce json string" should "convert User" in {
    assert(
      circeParcing.parseString("""{"id":1, "name": "John"}""") ==
        Right(User(1, "John"))
    )
    assert(
      circeParcing.parseString("""{"id":0, "name": "Bob"}""") ==
        Right(User(0, "Bob"))
    )
    assert(
      circeParcing.parseString("""{"id":0, "name": ""}""") ==
        Right(User(0, ""))
    )
  }

  "parce json option string" should "convert User" in {
    assert(
      circeParcing.parseString(Some("""{"id":1, "name": "John"}""")) ==
        Right(User(1, "John"))
    )
    assert(
      circeParcing.parseString(Some("""{"id":0, "name": "Bob"}""")) ==
        Right(User(0, "Bob"))
    )
    assert(
      circeParcing.parseString(Some("""{"id":0, "name": ""}""")) ==
        Right(User(0, ""))
    )
//    assert(
//      circeParcing.parseString(None) ==
//        Left(ParsingFailure("exhausted input", new Exception()))
//    )
  }

  "jsonObj" should "create" in {
    val field = Some("hoge").map(value => "id" -> Json.fromString(value)) ++
      Some("name_test_value").map(value => "name_value" -> Json.fromString(value))
    val jsonValue = Json
      .fromFields(field)
      .as[SomeOne]
    val expected = SomeOne("hoge", "name_test_value")
    assert(jsonValue == Right(expected))
  }

  case class SomeOne(id: String, name: String)
  implicit val decoderSome: Decoder[SomeOne] =
    Decoder.forProduct2("id", "name_value")(SomeOne)
  implicit val encoderSome: Encoder[SomeOne] =
    Encoder.forProduct2("id", "name_value")(someOne => (someOne.id, someOne.name))

}
