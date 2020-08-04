package jp.co.who.monad.eitherT

import scala.util._
import scala.concurrent._
import ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.io.Source

object FutureSample extends App {

  val url = "http://sample.com/"
  val f   = Future(HttpTextClient.get(url))

  f.onComplete {
    case Success(body) =>
      println("success")
      println(body.mkString)
      body.close
    case Failure(t) => t.printStackTrace()
  }
  Await.ready(f, duration.Duration.Inf)

}

import scala.io._
object HttpTextClient {
  def get(url: String): BufferedSource =
    Source.fromURL(url)
}
