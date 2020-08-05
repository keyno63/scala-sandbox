import io.finch._
import cats.effect.{IO, Sync}
import com.twitter.finagle.Http
import com.twitter.util.Await
import io.finch.DecodeEntity.decodeString
import shapeless.{:+:, CNil}


object App extends scala.App with Endpoint.Module[IO] {
  val endpoint = "api" :: "sample" :: Service.makeHttpService()

  val services = Bootstrap
    .serve[Text.Plain](endpoint)
    .toService

  val server = Http.server.serve(":8088", services)
  Await.ready(server)

//  val api: Endpoint[IO, String] = get("hello") { Ok("Hello, World!") }
//  Await.ready(Http.server.serve(":8080", api.toServiceAs[Text.Plain]))
  //Http.server.serve(":8080", api.toServiceAs[Text.Plain])

}

object Service extends Endpoint.Module[IO] {
  import io.finch._

  def makeHttpService()(implicit runtime: Sync[IO]): Endpoint[IO, String :+: String :+: CNil]=
    get(query) { query: String =>
      Ok(query)
    } :+: post(stringBody) { body: String =>
      Ok(body)
    }

  private val query = paramOption[String]("query")
  .mapAsync(query => IO.apply(query.getOrElse("none")))
}
