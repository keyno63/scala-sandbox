package com.github.who.zio

import zio.{ExitCode, IO, Task, UIO, URIO, ZIO}
import zio.console.Console

object CreateEffect extends App {

  val ids = List("id1", "nothing")
  for {
    v <- ids
    r = zioOption(v)
    _ <-
  } r

  def zioOption(id: String) = {
    // setUp
    type teamId = String
    case class User(id: String, name: String, teamId: teamId)
    case class Team(id: String, name: String)
    val users = List(
      User("id1", "Alice", "t1"),
      User("id2", "Bob", "t1"),
      User("id3", "Crith", "t2")
    )
    val teams = List(
      Team("t1", "Alice1"),
      Team("t2", "Crith1")
    )

    val maybeId: IO[Option[Nothing], String] = ZIO.fromOption(Some(id))
    def getUser(userId: String): IO[Throwable, Option[User]] =
      users
        .filter(_.id == userId)
        .head match {
        case u @ User(_, _, _) => ZIO.succeed(Some(u))
        case _ =>  ZIO.fail(new Exception(s"not found. id: $userId"))
      }
    def getTeam(teamId: String): IO[Throwable, Team] =
      teams
      .filter(_.id == teamId)
      .head match {
      case t @ Team(_, _) => ZIO.succeed(t)
      case _ =>  ZIO.fail(new Exception(s"not found. id: $teamId"))
    }

    val result: IO[Throwable, Option[(User, Team)]] = (for {
      id   <- maybeId
      user <- getUser(id).some
      team <- getTeam(user.teamId).asSomeError
    } yield (user, team)).optional
    result
  }
}
