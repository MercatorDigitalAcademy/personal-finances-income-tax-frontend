package controllers

import javax.inject._
import play.api.i18n.I18nSupport
import play.api.mvc._
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.IndexView
import views.html.ContinueView

/** This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject() (
    val controllerComponents: MessagesControllerComponents,
    view1: IndexView,
    view2: ContinueView
) extends FrontendBaseController
    with I18nSupport {

  def onPageLoad(): Action[AnyContent] = Action { implicit request =>
    Ok(view1())
  }

  def continue(): Action[AnyContent] = Action { implicit request =>
    Ok(view2())
  }
}
