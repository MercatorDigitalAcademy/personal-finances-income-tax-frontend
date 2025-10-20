package controllers

import controllers.actions._
import forms.AnnualIncomeFormProvider
import javax.inject.Inject
import models.Mode
import navigation.Navigator
import pages.AnnualIncomePage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.AnnualIncomeView

import scala.concurrent.{ExecutionContext, Future}

class AnnualIncomeController @Inject() (
    override val messagesApi: MessagesApi,
    sessionRepository: SessionRepository,
    navigator: Navigator,
    identify: IdentifierAction,
    getData: DataRetrievalAction,
    requireData: DataRequiredAction,
    formProvider: AnnualIncomeFormProvider,
    val controllerComponents: MessagesControllerComponents,
    view: AnnualIncomeView
)(implicit ec: ExecutionContext)
    extends FrontendBaseController
    with I18nSupport {

  val form = formProvider()

  def onPageLoad(): Action[AnyContent] =
    (identify andThen getData andThen requireData) { implicit request =>
      val preparedForm = request.userAnswers.get(AnnualIncomePage) match {
        case None        => form
        case Some(value) => form.fill(value)
      }

      Ok(view(preparedForm))
    }

  def onSubmit(): Action[AnyContent] =
    (identify andThen getData andThen requireData).async { implicit request =>
      form
        .bindFromRequest()
        .fold(
          formWithErrors =>
            Future.successful(BadRequest(view(formWithErrors))),
          value =>
            for {
              updatedAnswers <- Future
                .fromTry(request.userAnswers.set(AnnualIncomePage, value))
              _ <- sessionRepository.set(updatedAnswers)
            } yield Redirect(
              navigator.nextPage(AnnualIncomePage, updatedAnswers)
            )
        )
    }
}
