package controllers

import java.time.OffsetDateTime

import form.Post
import javax.inject.Inject
import play.api.data.Form
import play.api.data.Forms._
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.libs.json.Json
import play.api.libs.circe.Circe
import io.circe.syntax._
import io.circe.generic.auto._
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import repository.{Meta, PostRepository, Response}

case class PostRequests(body: String)

class PostsController @Inject()(cc: ControllerComponents, override val messagesApi: MessagesApi)
  extends AbstractController(cc)  with I18nSupport with Circe {

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
        BadRequest(Response(Meta(400, Some(errorMessage))).asJson)
      },
      postRequests => {
        val post = Post(postRequests.body, OffsetDateTime.now)
        PostRepository.add(post)
        //Ok(Json.toJson(Response(Meta(200))))
        Ok(Response(Meta(200)).asJson)
      }
    )
  }
}
