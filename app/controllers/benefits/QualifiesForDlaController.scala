package controllers.benefits

import controllers.actions._
import forms.benefits.QualifiesForDlaFormProvider
import models.{CheckMode, Mode, NormalMode}
import navigation.Navigator
import pages.benefits._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.QualifiesForDlaView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class QualifiesForDlaController @Inject() (
    override val messagesApi: MessagesApi,
    sessionRepository: SessionRepository,
    navigator: Navigator,
    identify: IdentifierAction,
    getData: DataRetrievalAction,
    requireData: DataRequiredAction,
    formProvider: QualifiesForDlaFormProvider,
    val controllerComponents: MessagesControllerComponents,
    view: QualifiesForDlaView
)(implicit ec: ExecutionContext)
    extends FrontendBaseController
    with I18nSupport {

  val form = formProvider()

  def onPageLoad(mode: Mode, index: Int): Action[AnyContent] =
    (identify andThen getData andThen requireData) { implicit request =>
      val preparedForm = mode match {
        case CheckMode =>
          request.userAnswers.get(ChildWithIndex(index)) match {
            case Some(child) => form.fill(child.qualifiesForDLA)
            case None =>
              request.userAnswers.get(QualifiesForDlaPage(index)) match {
                case Some(dob) => form.fill(dob)
                case None      => form
              }

          }
        case NormalMode =>
          request.userAnswers.get(QualifiesForDlaPage(index)) match {
            case None        => form
            case Some(value) => form.fill(value)
          }
      }

      Ok(view(preparedForm, mode, index))
    }

  def onSubmit(mode: Mode, index: Int): Action[AnyContent] =
    (identify andThen getData andThen requireData).async { implicit request =>
      form
        .bindFromRequest()
        .fold(
          formWithErrors =>
            Future.successful(BadRequest(view(formWithErrors, mode, index))),
          qualifies => {
            request.userAnswers
              .set(QualifiesForDlaPage(index), qualifies) match {
              case Success(updatedUa) =>
                if (qualifies) {
                  for {
                    setAnswers <- Future.fromTry(
                      request.userAnswers
                        .set(QualifiesForDlaPage(index), qualifies)
                    )
                    _ <- sessionRepository.set(setAnswers)
                  } yield Redirect(
                    navigator.nextPage(
                      QualifiesForDlaPage(index),
                      mode,
                      updatedUa,
                      index
                    )
                  )

                } else {
                  for {
                    setAnswers <- Future.fromTry(
                      request.userAnswers
                        .set(QualifiesForDlaPage(index), qualifies)
                    )
                    updatedAnswers <- Future
                      .fromTry(setAnswers.remove(DlaRatePage(index)))
                    _ <- sessionRepository.set(updatedAnswers)
                  } yield Redirect(
                    controllers.benefits.routes.CheckYourAnswersController
                      .onPageLoad(index)
                  )

                  sessionRepository.set(updatedUa).map { _ =>
                    request.userAnswers.remove(DlaRatePage(index))
                    Redirect(
                      controllers.benefits.routes.CheckYourAnswersController
                        .onPageLoad(index)
                    )
                  }
                }

              case Failure(_) =>
                Future.successful(InternalServerError("Could not save answer"))
            }
          }
        )
    }
}
