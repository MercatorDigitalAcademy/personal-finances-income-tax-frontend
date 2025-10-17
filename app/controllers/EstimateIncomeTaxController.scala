package controllers

import controllers.actions._
import javax.inject.Inject
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.EstimateIncomeTaxView

class EstimateIncomeTaxController @Inject()(
                                             override val messagesApi: MessagesApi,
                                             identify: IdentifierAction,
                                             val controllerComponents: MessagesControllerComponents,
                                             view: EstimateIncomeTaxView
                                           ) extends FrontendBaseController with I18nSupport {

  def onPageLoad: Action[AnyContent] = identify { implicit request =>
    Ok(view())
  }
}
