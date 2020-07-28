package com.github.keyno.http4s

import cats.effect.{ConcurrentEffect, ContextShift, ExitCode, IO, IOApp, Timer}
import cats.implicits._
import fs2.Stream
import org.http4s.server.blaze.BlazeServerBuilder

import scala.concurrent.ExecutionContext.global
import cats.effect.Sync
import org.http4s.{HttpRoutes, MediaType}
import org.http4s.dsl.Http4sDsl
import org.http4s.server.middleware.Logger
import org.http4s.implicits._
import org.log4s.getLogger


object ExampleApp extends IOApp {
  val logger = getLogger(this.getClass)
  val host = "0.0.0.0"
  val port = 8080

  def run(args: List[String]) = {
    stream[IO].compile.drain.as(ExitCode.Success)
  }


  def stream[F[_]: ConcurrentEffect](implicit T: Timer[F], C: ContextShift[F]): Stream[F, Nothing] = {

    val httpApp = (Route.echoRoutes[F]()).orNotFound
    val finalHttpApp = Logger.httpApp(true, true)(httpApp)

    for {
      exitCode <- BlazeServerBuilder[F](global)
        .bindHttp(port, host)
        .withHttpApp(finalHttpApp)
        .serve
    } yield exitCode
  }.drain

}

object Route {
  val `application/graphql` = new MediaType("application", "graphql")

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

  def echoRoutes[F[_]: Sync](): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case req @ POST -> Root / "echo" =>
        if (req.contentType.map(_.mediaType).contains(`application/graphql`))
        for {
          greeting <- req.bodyText.pure[F]
          //ret = Stream.eval("graphql") <+> greeting
          resp <- Ok(greeting)
        } yield resp
        else
          for {
            greeting <- req.bodyText.pure[F]
            resp <- Ok(greeting )
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
