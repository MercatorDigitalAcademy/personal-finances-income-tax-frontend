package controllers.benefits

import controllers.actions._
import forms.benefits.ChildsNameFormProvider
import models.{CheckMode, Mode, NormalMode}
import navigation.Navigator
import pages.benefits.{ChildWithIndex, ChildsNamePage}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.ChildsNameView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class ChildsNameController @Inject() (
    override val messagesApi: MessagesApi,
    sessionRepository: SessionRepository,
    navigator: Navigator,
    identify: IdentifierAction,
    getData: DataRetrievalAction,
    requireData: DataRequiredAction,
    formProvider: ChildsNameFormProvider,
    val controllerComponents: MessagesControllerComponents,
    view: ChildsNameView
)(implicit ec: ExecutionContext)
    extends FrontendBaseController
    with I18nSupport {

  val form = formProvider()

  def onPageLoad(mode: Mode, index: Int): Action[AnyContent] =
    (identify andThen getData andThen requireData) { implicit request =>
      val nameFromForm = request.userAnswers.get(ChildsNamePage(index))
      val childFromList = request.userAnswers.get(ChildWithIndex(index))
      val preparedForm = mode match {
        case CheckMode =>
          childFromList match {
            case None        => nameFromForm match {
              case Some(name) => form.fill(name)
              case None => form
            }
            case Some(value) => form.fill(value.name)

          }
        case NormalMode =>
          nameFromForm match {
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
          value =>
            for {
              updatedAnswers <- Future
                .fromTry(request.userAnswers.set(ChildsNamePage(index), value))
              _ <- sessionRepository.set(updatedAnswers)
            } yield Redirect(
              navigator
                .nextPage(ChildsNamePage(index), mode, updatedAnswers, index)
            )
        )
    }
}
