package controllers

import javax.inject.{ Inject, Singleton }
import play.api.i18n.{ I18nSupport, Messages }
import play.api.mvc._

@Singleton
class HelloController @Inject() (cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {

  def get(name: Option[String]): Action[AnyContent] =
    Action { implicit request: Request[AnyContent] =>
      Ok {
        name
          .map(s => Messages(s"Hello, $s!", s))
          .getOrElse(Messages("""Please give a name as a query parameter named "name"."""))
      }
    }

  def plus(a: Option[Int], b: Option[Int]): Action[AnyContent] =
    Action { implicit request: Request[AnyContent] =>
      Ok {
        val x = (a, b) match {
          case (Some(y), Some(z)) => y + z
          case (Some(y), _)       => y
          case (_, Some(z))       => z
          case (_, _)             => 0
        }
        s"$x"
      }
    }
}
