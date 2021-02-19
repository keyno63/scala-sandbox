import cats.effect.{ ExitCode, IO, IOApp, Resource }
import domain.Country
import skunk.{ ~, Query, Session }
import skunk.implicits.toStringOps
import skunk.codec.all.{ date, int4, varchar }
import natchez.Trace.Implicits.noop

object App extends IOApp {

  type Return = IO[ExitCode]

  val session: Resource[IO, Session[IO]] =
    Session.single(
      host = "localhost",
      port = 15432,
      user = "root",
      database = "world",
      password = Some("password")
    )

  def run(args: List[String]): Return =
    multiColumnQuerySample

  def parameterizedQuery(): IO[Unit] = {
    val e: Query[String, Country] =
      sql"""
    SELECT name, population
    FROM   country
    WHERE  name LIKE $varchar
  """.query(country)

    session.use { s =>
      s.prepare(e).use { ps =>
        ps.stream("U%", 64)
          .evalMap(c => IO(println(c)))
          .compile
          .drain
      }
    }

  }

  def multiColumnQuerySample(): Return = {
    val multiColumnQuery: Query[skunk.Void, String ~ Int] =
      sql"SELECT name, population FROM country".query(varchar ~ int4)
    session.use { s =>
      for {
        // List で複数データを返すために execute を呼ぶ
        d <- s.execute(multiColumnQuery)
        _ <- IO(println(s"The current is $d"))
      } yield ExitCode.Success
    }
  }

  def querySample(): Return = {
    val query: Query[skunk.Void, java.time.LocalDate] = sql"select current_date".query(date)
    session.use { s =>
      for {
        // 単一カラムなので unique を呼ぶ
        d <- s.unique(query)
        _ <- IO(println(s"The current date is $d."))
      } yield ExitCode.Success
    }
  }
}
