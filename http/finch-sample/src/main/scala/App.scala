import io.finch._
import cats.effect.{IO, Sync}
import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.util.Await
import io.finch.DecodeEntity.decodeString
import shapeless.{:+:, CNil}

object App extends scala.App with Endpoint.Module[IO] {
  val endpoint = "api" :: "sample" :: Service.makeHttpService()

  val services = Bootstrap
    .serve[Text.Plain](endpoint)
    .toService

  val server: ListeningServer = Http.server.serve(":8080", services)
  Await.ready(server)

}

object Service extends Endpoint.Module[IO] {
  import io.finch._

  def makeHttpService()(implicit runtime: Sync[IO]): Endpoint[IO, String :+: String :+: CNil] =
    get(query) { query: Option[String] =>
      query match {
        case Some(id) =>
          UserAccountRepository.find(id) match {
            case Some(acount) => Ok(acount.toString)
            case _ => NotFound(new Exception(s"not found account, id: $id"))
          }
        case _ => BadRequest(new Exception("query is invalid"))
      }
    } :+: post(stringBody:: header("content-type") ) {
      (body: String, header: String) =>
        (body, header) match {
          case (_, "application/json") =>
            Ok(s"Json request succeed to json $body")
          case (_, "application/x-www-form-urlencoded") =>
            Ok(s"UrlEncode request succeed to json $body")
          case _ => BadRequest(new Exception("request is invalid"))
        }
    }

  private val query =
    paramOption[String]("id")
    .mapAsync(query => IO.apply(query))
}

case class UserAccount(id: String, name: String) {
  override def toString: String = s"id: $id, name: $name"
}

object UserAccountRepository {
  private val userAccountList = List(
    UserAccount("0", "Alice"),
    UserAccount("1", "Bob"),
    UserAccount("2", "Crith"),
  )

  def find(id: String): Option[UserAccount] = {
    userAccountList.filter(_.id == id).head match {
      case ret @ UserAccount(_, _) => Some(ret)
      case _ => None
    }
  }

  def update(account: UserAccount): Either[Throwable, UserAccount] = {
    // sample なので常に代入する Right を返す.
    Right(account)
  }
}