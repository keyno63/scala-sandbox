package com.github.who.zio

import com.github.who.zio.CreateEffect.{ teamId, zioEither, zioOption }
import io.circe.generic.auto._
import io.circe.parser._
import java.io

import zio.{ IO, ZIO }

case class EffectUser(id: String, name: String, teamId: teamId)
case class Team(id: String, name: String)

object CreateEffect {
  // private
  type teamId = String
  // setUp
  val users = List(
    EffectUser("id1", "Alice", "t1"),
    EffectUser("id2", "Bob", "t3"),
    EffectUser("id3", "Crith", "t2")
  )
  val teams = List(
    Team("t1", "Alice1"),
    Team("t2", "Crith1")
  )

  private def getUser(userId: String): IO[Throwable, Option[EffectUser]] =
    users.find(_.id == userId) match {
      case u @ Some(_) => ZIO.succeed(u)
      case _           => ZIO.fail(new Exception(s"not found user. id: $userId"))
    }
  private def getTeam(teamId: String): IO[Throwable, Team] =
    teams.find(_.id == teamId) match {
      case Some(t) => ZIO.succeed(t)
      case _       => ZIO.fail(new Exception(s"not found team. id: $teamId"))
    }

  def zioOption(id: String): IO[Throwable, Option[(EffectUser, Team)]] = {
    val maybeId: IO[Option[Nothing], String] = ZIO.fromOption(Some(id))

    val result: IO[Throwable, Option[(EffectUser, Team)]] = (for {
      id   <- maybeId
      user <- getUser(id).some
      team <- getTeam(user.teamId).asSomeError
    } yield (user, team)).optional
    result
  }

  def zioEither(jsonString: String): IO[io.Serializable, Option[(EffectUser, Team)]] = {
    val json        = parse(jsonString).flatMap(_.as[EffectUser])
    val maybeEither = ZIO.fromEither(json)
    val ret = for {
      user <- maybeEither
      team <- getTeam(user.teamId).asSomeError
    } yield (user, team)
    ret.option
  }
}

object CreateSample extends zio.BootstrapRuntime with scala.App {
  // option
  val ids = List("id1", "id2", "nothing")
  for {
    v <- ids
    r = zioOption(v)
  } println(
    unsafeRun(
      r.catchAll(e => ZIO.succeed(e))
    )
  )

  // either
  val jsonStr: String =
    """
      |{
      |  "id": "1",
      |  "name": "Alice",
      |  "teamId": "t1"
      |}
      |""".stripMargin
  val ze = zioEither(jsonStr)
  println(unsafeRun(ze))
}
