package controllers

import controllers.actions._
import forms.IsOverPensionAgeFormProvider
import javax.inject.Inject
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.IsOverPensionAgeView

import scala.concurrent.ExecutionContext

class IsOverPensionAgeController @Inject()(
                                            override val messagesApi: MessagesApi,
                                            identify: IdentifierAction,
                                            val controllerComponents: MessagesControllerComponents,
                                            view: IsOverPensionAgeView,
                                            formProvider: IsOverPensionAgeFormProvider
                                          )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  private val form = formProvider()

  def onPageLoad(): Action[AnyContent] = identify { implicit request =>
    Ok(view(form))
  }

  def onSubmit(): Action[AnyContent] = identify { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors => BadRequest(view(formWithErrors)),
      isOverPensionAge => {
        val incomeOpt = request.session.get("income")
        val yearOpt = request.session.get("taxYear")

        (incomeOpt, yearOpt) match {
          case (Some(income), Some(year)) =>
            Redirect(routes.EstimateIncomeTaxController.onPageLoad())
              .flashing(
                "income" -> income,
                "taxYear" -> year,
                "isOverPensionAge" -> isOverPensionAge.toString
              )
              .removingFromSession("income", "taxYear")(request)

          case _ =>
            Redirect(routes.TaxFormController.onPageLoad())
        }
      }
    )
  }
}
