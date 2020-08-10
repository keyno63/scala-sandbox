import cats.effect.{ IO, Sync }
import com.twitter.finagle.{ Http, ListeningServer }
import com.twitter.util.Await
import io.finch.{ Bootstrap, Endpoint, Text }

object App2 extends scala.App with Endpoint.Module[IO] {
  val endpoint = "api" :: "sample" :: Service2.make

  val services = Bootstrap
    .serve[Text.Plain](endpoint)
    .toService

  val server: ListeningServer = Http.server.serve(":8080", services)
  Await.ready(server)
}

object Service2 extends Endpoint.Module[IO] {
  import io.finch.{ BadRequest, Endpoint, Ok }
  import shapeless.{ :+:, CNil }
  def make()(implicit runtime: Sync[IO]): Endpoint[IO, String :+: String :+: CNil] =
    get(query) { query: Option[String] =>
      Ok("get request")
    } :+: post(stringBodyOption :: headerOption("content-type")) { (body: Option[String], header: Option[String]) =>
      (body, header) match {
        case (_, Some(value)) => Ok(s"ok. header: $value, body: ${body.getOrElse("no body")}")
        case _           => BadRequest(new Exception(s"invalid request. header: $header, body: $body"))
      }
    }

  private val query =
    paramOption[String]("id")
      .mapAsync(query => IO.apply(query))
}
