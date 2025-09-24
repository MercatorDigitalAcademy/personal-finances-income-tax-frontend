package controllers.benefits

import controllers.actions._
import forms.benefits.ChildsBirthDateFormProvider
import models.Mode
import navigation.Navigator
import pages.benefits.{ChildsBirthDatePage, ChildsNamePage}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.ChildsBirthDateView

import javax.inject.Inject
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
      val childsName: String = request.userAnswers.get(ChildsNamePage).getOrElse("this child.")

      Ok(view(preparedForm, mode, childsName))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>

      val form = formProvider()
      val childsName: String = request.userAnswers.get(ChildsNamePage).getOrElse("this child" +
        ".")

      form.bindFromRequest().fold(
        formWithErrors =>
          Future.successful(BadRequest(view(formWithErrors, mode, childsName))),

        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(ChildsBirthDatePage, value))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(ChildsBirthDatePage, mode, updatedAnswers))
      )
  }
}
