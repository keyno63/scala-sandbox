package com.github.keyno.http4s

import cats.effect.{ConcurrentEffect, ContextShift, ExitCode, IO, IOApp, Timer}
import cats.implicits._
import fs2.Stream
import org.http4s.{HttpRoutes, HttpService, Response, Status}
import org.http4s.server.blaze.{BlazeBuilder, BlazeServerBuilder}

import scala.concurrent.ExecutionContext.global
import cats.effect.Sync
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
import org.http4s.server.middleware.Logger
import org.log4s.getLogger
//import io.circe.{Encoder, Json}
import org.http4s.implicits._


object ExampleApp extends IOApp {
  val logger = getLogger(this.getClass)


  def run(args: List[String]) = {
    stream[IO].compile.drain.as(ExitCode.Success)
  }

  def stream[F[_]: ConcurrentEffect](implicit T: Timer[F], C: ContextShift[F]): Stream[F, Nothing] = {

    // to create App
    val httpApp = Route.helloWorldRoutes[F]().orNotFound
    val finalHttpApp = Logger.httpApp(true, true)(httpApp)

    for {
      exitCode <- BlazeServerBuilder[F](global)
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(finalHttpApp)
        .serve
    } yield exitCode
  }.drain


}

object Route {
  def helloWorldRoutes[F[_]: Sync](): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "hello" / name =>
        for {
          greeting <- name.pure[F]
          resp <- Ok(greeting)
        } yield resp
    }
  }

  // T.B.D.
  /*
  def toSimpleJson(key: String, value: String) = {
    Json.obj(
      ("message", Json.fromString(name)),
    )
  }
   */
}
