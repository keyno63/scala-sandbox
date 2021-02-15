import cats.effect.{ ExitCode, IO, IOApp, Resource }
import skunk.Session
import skunk.implicits.toStringOps
import skunk.codec.all.{ date, int4, varchar }
import natchez.Trace.Implicits.noop

object App extends IOApp {

  val session: Resource[IO, Session[IO]] =
    Session.single(
      host = "localhost",
      port = 15432,
      user = "root",
      database = "world",
      password = Some("password")
    )

  def run(args: List[String]): IO[ExitCode] =
    multiColumnQuerySample

  def multiColumnQuerySample(): IO[ExitCode] =
    session.use { s =>
      for {
        d <- s.execute(sql"SELECT name, population FROM country".query(varchar ~ int4))
        _ <- IO(println(s"The current is $d"))
      } yield ExitCode.Success
    }

  def querySample(): IO[ExitCode] =
    session.use { s =>
      for {
        d <- s.unique(sql"select current_date".query(date))
        _ <- IO(println(s"The current date is $d."))
      } yield ExitCode.Success
    }
}
