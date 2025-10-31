package controllers

import controllers.actions._
import javax.inject.Inject
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.TaxFormView
import forms.{TaxFormData, TaxFormFormProvider}
import java.time.{LocalDate, Month}

class TaxFormController @Inject()(
                                   override val messagesApi: MessagesApi,
                                   identify: IdentifierAction,
                                   val controllerComponents: MessagesControllerComponents,
                                   view: TaxFormView,
                                   formProvider: TaxFormFormProvider
                                 ) extends FrontendBaseController with I18nSupport {

  private def currentUkTaxYearEnd: Int = {
    val today = LocalDate.now()
    if (today.isBefore(LocalDate.of(today.getYear, Month.APRIL, 6))) today.getYear else today.getYear + 1
  }

  private def taxYearLabels(range: Int = 6): Seq[String] = {
    val end = currentUkTaxYearEnd
    (0 until range).map { i =>
      val endYear = end - i
      f"${endYear - 1}–${endYear % 100}%02d" // e.g. 2024–25
    }
  }

  private def form = formProvider(taxYearLabels())

  def onPageLoad(): Action[AnyContent] = identify { implicit request =>
    Ok(view(form, taxYearLabels()))
  }

  def onSubmit(): Action[AnyContent] = identify { implicit request =>
    val years = taxYearLabels()
    formProvider(years).bindFromRequest().fold(
      errs => BadRequest(view(errs, years)),
      data => {
        Redirect(routes.IsOverPensionAgeController.onPageLoad())
          .addingToSession(
            "income" -> data.annualIncome.bigDecimal.toPlainString,
            "taxYear" -> data.taxYear
          )
      }
    )
  }
}
