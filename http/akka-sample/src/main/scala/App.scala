import java.net.http.HttpHeaders

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentType, HttpHeader}
import akka.http.scaladsl.server.Directives._

import scala.concurrent.Future

object Apps extends scala.App {
  val x = ContentType.parse("Content-Type: application/graphql")
  println(x)
}
object App extends scala.App {

  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher

  val host = "localhost"
  val port = 8080

  val route =
    path( "graphql") {
      httpHeaderRoute
    } ~ path("test") {
      printRoute
    }

  val bindingFuture: Future[Http.ServerBinding] = Http().bindAndHandle(route, host, port)

  val httpHeaderRoute = {
    (get & headerValue {
      case HttpHeader("content-type", value) => Some(value)
      case _                                   => None
    }) {
      case "application/graphql" => complete("this is target!!!")
      case x => complete(s"this is have content-type, ${x}")
      case _ => complete("")
    } ~ get {
      complete("not content-type")
    }
  }

  val printRoute = {
    (get & headerValue(hoge)) {
      case x => complete(x.toString())
      case _ => complete("ok")
    }
  }
  def hoge : HttpHeader => Option[HttpHeader] = { x =>
    println(x)
    Some(x)
    /*
    x match {
      case HttpHeader("content-type", value) => Some(value)
      case _ => None
    }*/
  }

  def headerValueByNameRoute = {
    get {
      headerValueByName("content-type") {
        req => complete(s"ok. ${req.toString}")
      }
    } ~ get {
      complete("ok")
    }
  }
}
