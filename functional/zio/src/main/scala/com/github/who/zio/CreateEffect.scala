package com.github.who.zio

import com.github.who.zio.Main.unsafeRun
import zio.{ IO, ZIO }

object CreateEffect extends App {

  val ids = List("id1", "id2", "nothing")
  for {
    v <- ids
    r = zioOption(v)
  } println(
    unsafeRun(
      r.catchAll(e => ZIO.succeed(e))
    )
  )

  def zioOption(id: String) = {
    // setUp
    type teamId = String
    case class User(id: String, name: String, teamId: teamId)
    case class Team(id: String, name: String)
    val users = List(
      User("id1", "Alice", "t1"),
      User("id2", "Bob", "t3"),
      User("id3", "Crith", "t2")
    )
    val teams = List(
      Team("t1", "Alice1"),
      Team("t2", "Crith1")
    )

    val maybeId: IO[Option[Nothing], String] = ZIO.fromOption(Some(id))
    def getUser(userId: String): IO[Throwable, Option[User]] =
      users.find(_.id == userId) match {
        case u @ Some(_) => ZIO.succeed(u)
        case _           => ZIO.fail(new Exception(s"not found user. id: $userId"))
      }
    def getTeam(teamId: String): IO[Throwable, Team] =
      teams.find(_.id == teamId) match {
        case Some(t) => ZIO.succeed(t)
        case _       => ZIO.fail(new Exception(s"not found team. id: $teamId"))
      }

    val result: IO[Throwable, Option[(User, Team)]] = (for {
      id   <- maybeId
      user <- getUser(id).some
      team <- getTeam(user.teamId).asSomeError
    } yield (user, team)).optional
    result
  }
}
