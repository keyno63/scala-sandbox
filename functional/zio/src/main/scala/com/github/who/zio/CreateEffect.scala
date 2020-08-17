package com.github.who.zio

import com.github.who.zio.CreateEffect.{ teamId, zioOption }
import zio.{ IO, ZIO }

case class EffectUser(id: String, name: String, teamId: teamId)
case class Team(id: String, name: String)

object CreateEffect {
  type teamId = String

  def zioOption(id: String): IO[Throwable, Option[(EffectUser, Team)]] = {
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

    val maybeId: IO[Option[Nothing], String] = ZIO.fromOption(Some(id))
    def getUser(userId: String): IO[Throwable, Option[EffectUser]] =
      users.find(_.id == userId) match {
        case u @ Some(_) => ZIO.succeed(u)
        case _           => ZIO.fail(new Exception(s"not found user. id: $userId"))
      }
    def getTeam(teamId: String): IO[Throwable, Team] =
      teams.find(_.id == teamId) match {
        case Some(t) => ZIO.succeed(t)
        case _       => ZIO.fail(new Exception(s"not found team. id: $teamId"))
      }

    val result: IO[Throwable, Option[(EffectUser, Team)]] = (for {
      id   <- maybeId
      user <- getUser(id).some
      team <- getTeam(user.teamId).asSomeError
    } yield (user, team)).optional
    result
  }
}

object CreateSample extends zio.BootstrapRuntime with scala.App {
    val ids = List("id1", "id2", "nothing")
    for {
      v <- ids
      r = zioOption(v)
    } println(unsafeRun(
        r.catchAll(e => ZIO.succeed(e))
      ))
}
