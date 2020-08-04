package com.github.who.zio

import java.io.IOException

import zio.{ ExitCode, Task, URIO, ZIO }
import zio.console._

// https://zio.dev/docs/getting_started.html
object Main extends zio.App {
  def run(args: List[String]): URIO[Console, ExitCode] = {
    //myAppLogic.exitCode
    Task(User(1, "name"))
    //.foldCause(_ => User(0, "none"), _)
    .exitCode
    myEitherLogic.exitCode
  }

  val myAppLogic: ZIO[Console, IOException, Unit] =
    for {
      _    <- putStrLn("Hello! What is your name?")
      name <- getStrLn
      _    <- putStrLn(s"Hello, ${name}, welcome to ZIO!")
    } yield ()

  val myEitherLogic: ZIO[Console, Exception, Unit] = {
    for {
      _    <- putStrLn("Hello! What is your name?")
      name <- getStrLn
      ret  <- ZIO.fromEither(func1(name))
      //retStr <- ret.flatMap(s => s"input: $s")
      _ <- putStrLn(s"Hello, ${name}, $ret welcome to ZIO!")
    } yield ()
  }

  def func1(name: String): Either[Exception, String] =
    name match {
      case "hoge" => Left(new Exception("failed"))
      case _      => Right(name)
    }
}

case class User(id: Int, name: String)
