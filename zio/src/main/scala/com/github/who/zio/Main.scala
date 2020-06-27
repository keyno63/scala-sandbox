package com.github.who.zio

import java.io.IOException

import zio.ZIO
import zio.console._

// https://zio.dev/docs/getting_started.html
object Main extends zio.App {
  def run(args: List[String]) =
    myAppLogic.exitCode

  val myAppLogic: ZIO[Console, IOException, Unit] =
    for {
      _    <- putStrLn("Hello! What is your name?")
      name <- getStrLn
      _    <- putStrLn(s"Hello, ${name}, welcome to ZIO!")
    } yield ()
}
