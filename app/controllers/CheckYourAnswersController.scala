package controllers

import controllers.actions._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.CheckYourAnswersView

import javax.inject.Inject

class CheckYourAnswersController @Inject()(override val messagesApi: MessagesApi, identify: IdentifierAction, val controllerComponents: MessagesControllerComponents, view: CheckYourAnswersView) extends FrontendBaseController with I18nSupport {

  def onPageLoad(): Action[AnyContent] = identify { implicit request =>
    val incomeOpt = request.session.get("income")
    val taxYearOpt = request.session.get("taxYear")
    val isOverPensionOpt = request.session.get("isOverPensionAge")

    (incomeOpt, taxYearOpt, isOverPensionOpt) match {
      case (Some(income), Some(year), Some(pension)) => Ok(view(income, year, pension.toBoolean))
      case _ => Redirect(routes.TaxFormController.onPageLoad())
    }
  }

  def onSubmit(): Action[AnyContent] = identify { implicit request =>
    val incomeOpt = request.session.get("income")
    val taxYearOpt = request.session.get("taxYear")
    val isOverPensionOpt = request.session.get("isOverPensionAge")

    (incomeOpt, taxYearOpt, isOverPensionOpt) match {
      case (Some(income), Some(year), Some(pension)) => Redirect(routes.EstimateIncomeTaxController.onPageLoad()).flashing("income" -> income, "taxYear" -> year, "isOverPensionAge" -> pension).removingFromSession("income", "taxYear", "isOverPensionAge")(request)
      case _ => Redirect(routes.TaxFormController.onPageLoad())
    }
  }
}
