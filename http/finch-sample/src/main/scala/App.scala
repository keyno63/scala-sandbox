import io.finch._
import io.finch.Endpoint._
import io.circe.Json
import cats.effect.IO
import com.twitter.finagle.Http
import com.twitter.util.Await

object App extends scala.App with Endpoint.Module[IO] {
}
