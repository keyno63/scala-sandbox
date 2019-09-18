package controllers

import java.time.OffsetDateTime

import javax.inject.Inject
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.libs.json.Json
import io.circe._
import io.circe.syntax._
import io.circe.generic.auto._
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

case class PostRequests(body: String)

class PostsController @Inject()(cc: ControllerComponents, override val messagesApi: MessagesApi) extends AbstractController(cc)  with I18nSupport {

  private[this] val form = Form(
    mapping(
      "post" -> text(minLength = 1, maxLength = 10)
    )(PostRequests.apply)(PostRequests.unapply))

  def get: Action[AnyContent] = Action { implicit request =>
    Ok(Json.toJson(Response(Meta(200), Some(Json.obj("posts" -> Json.toJson(PostRepository.findAll))))))
    //Ok(Response(Meta(200), Some(Json.obj("posts" -> Json.toJson(PostRepository.findAll)))).asJson)
  }

  def post: Action[AnyContent] = Action { implicit request =>
    form.bindFromRequest.fold(
      error => {
        val errorMessage = Messages(error.errors("post").head.message)
        BadRequest(Json.toJson(Response(Meta(400, Some(errorMessage)))))
      },
      postRequests => {
        val post = Post(postRequests.body, OffsetDateTime.now)
        PostRepository.add(post)
        Ok(Json.toJson(Response(Meta(200))))
      }
    )
  }
}
