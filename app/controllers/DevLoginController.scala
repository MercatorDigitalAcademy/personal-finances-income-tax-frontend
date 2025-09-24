package controllers

import play.api.i18n.I18nSupport
import play.api.mvc._
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.DevLoginView

import javax.inject.{Inject, Singleton}

@Singleton
class DevLoginController @Inject() (
    val controllerComponents: MessagesControllerComponents,
    devLoginView: DevLoginView
) extends FrontendBaseController
    with I18nSupport {

  def showLogin(): Action[AnyContent] = Action {
    implicit request: MessagesRequest[AnyContent] =>
      Ok(devLoginView())
  }

  def submitLogin(): Action[AnyContent] = Action { implicit request =>
    request.body.asFormUrlEncoded.flatMap(
      _.get("username").flatMap(_.headOption)
    ) match {
      case Some(username) =>
        Redirect(controllers.benefits.routes.IndexController.onPageLoad())
          .withSession("userId" -> username)
      case None =>
        Redirect(routes.DevLoginController.showLogin())
          .flashing("error" -> "Please enter a username")
    }
  }
}
