package controllers

import navigation.Navigator
import pages.DevLoginPage
import play.api.i18n.I18nSupport
import play.api.mvc._
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.DevLoginView
import play.api.Logging

import javax.inject.{Inject, Singleton}

@Singleton
class DevLoginController @Inject() (
    val controllerComponents: MessagesControllerComponents,
    devLoginView: DevLoginView,
    navigator: Navigator
) extends FrontendBaseController
    with I18nSupport with Logging {

  def showLogin(): Action[AnyContent] = Action {
    implicit request: MessagesRequest[AnyContent] =>
      Ok(devLoginView())
  }

  def submitLogin(): Action[AnyContent] = Action { implicit request =>
    val ua = request.body.asFormUrlEncoded.flatMap(
      _.get("username").flatMap(_.headOption)
    )
    ua match {
      case Some(username) => {
        println(s"logging in with username: $username")
        logger.info(s"logging in with username: $username")
        Redirect(navigator.nextPage(DevLoginPage))
          .withSession("userId" -> username)
      }
      case None => {
        {
          println("not able to get username")
          logger.info(s"not able to get username")

          Redirect(routes.DevLoginController.showLogin())
            .flashing("error" -> "Please enter a username")
        }
      }
    }
  }
}
