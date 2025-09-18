package controllers

import controllers.actions._
import forms.ChildsBirthDateFormProvider
import javax.inject.Inject
import models.Mode
import navigation.Navigator
import pages.ChildsBirthDatePage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.ChildsBirthDateView

import scala.concurrent.{ExecutionContext, Future}

class ChildsBirthDateController @Inject()(
                                        override val messagesApi: MessagesApi,
                                        sessionRepository: SessionRepository,
                                        navigator: Navigator,
                                        identify: IdentifierAction,
                                        getData: DataRetrievalAction,
                                        requireData: DataRequiredAction,
                                        formProvider: ChildsBirthDateFormProvider,
                                        val controllerComponents: MessagesControllerComponents,
                                        view: ChildsBirthDateView
                                      )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>

      val form = formProvider()

      val preparedForm = request.userAnswers.get(ChildsBirthDatePage) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      Ok(view(preparedForm, mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>

      val form = formProvider()

      form.bindFromRequest().fold(
        formWithErrors =>
          Future.successful(BadRequest(view(formWithErrors, mode))),

        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(ChildsBirthDatePage, value))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(ChildsBirthDatePage, mode, updatedAnswers))
      )
  }
}
