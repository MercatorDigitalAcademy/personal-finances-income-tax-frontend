package controllers

import com.google.inject.Inject
import config.FrontendAppConfig
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController

import scala.concurrent.ExecutionContext

class LogoutController @Inject()(
                                  appConfig: FrontendAppConfig,
                                  val controllerComponents: MessagesControllerComponents
                                )(implicit val ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  def onPageLoad: Action[AnyContent] = Action {
    request =>
      Redirect(appConfig.signOutUrl)
  }

}