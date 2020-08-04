import io.circe.ParsingFailure
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

}
