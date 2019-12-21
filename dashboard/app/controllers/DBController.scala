package controllers

import java.time.OffsetDateTime

import form.Post
import javax.inject.Inject
import play.api.data.Form
import play.api.data.Forms.{mapping, text}
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import repository.{Meta, PostPgsqlRepository, Response}

class DBController @Inject()(cc: ControllerComponents, override val messagesApi: MessagesApi)
  extends AbstractController(cc)  with I18nSupport {
  private[this] val form = Form(
    mapping(
      "post" -> text(minLength = 1, maxLength = 10)
    )(PostRequests.apply)(PostRequests.unapply))

  def get: Action[AnyContent] = Action { implicit request =>
    Ok(
      Json.toJson(
        Response(
          Meta(200),
          Some(
            Json.obj("posts" -> Json.toJson(PostPgsqlRepository.findAll))
          )
        )
      )
    )
  }

  def post: Action[AnyContent] = Action { implicit request =>
    form.bindFromRequest.fold(
      error => {
        val errorMessage = Messages(error.errors("post").head.message)
        BadRequest(
          Json.toJson(
            Response(
              Meta(400),
              Some(
                Json.obj("error" -> Json.toJson(errorMessage))
              )
            )
          )
        )
      },
      postRequests => {
        val post = Post(postRequests.body, OffsetDateTime.now)
        PostPgsqlRepository.add(post)
        Ok(
          Json.toJson(Response(Meta(200)))
        )
      }
    )
  }

  def get2(): Action[AnyContent] = Action { implicit request =>
    Ok("")
  }

  def post2(): Action[AnyContent] = Action { implicit request =>
    Ok("")
  }

  def get3(): Action[AnyContent] = Action { implicit request =>
    Ok("")
  }

  def post3(): Action[AnyContent] = Action { implicit request =>
    Ok("")
  }



}
