package controllers.benefits

import controllers.actions._
import forms.benefits.AddAChildFormProvider
import models.{CannotSaveChildError, Mode, UserAnswers}
import navigation.Navigator
import pages.benefits.{
  ChildGroup,
  ChildsBirthDatePage,
  ChildsNamePage,
  DlaRatePage
}
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.AddAChildSummary
import viewmodels.govuk.all.SummaryListViewModel
import views.html.AddAChildView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class AddAChildController @Inject() (
    identify: IdentifierAction,
    getData: DataRetrievalAction,
    requireData: DataRequiredAction,
    sessionRepository: SessionRepository,
    formProvider: AddAChildFormProvider,
    navigator: Navigator,
    val controllerComponents: MessagesControllerComponents,
    view: AddAChildView
)(implicit ec: ExecutionContext)
    extends FrontendBaseController
    with I18nSupport {

  private val form = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] =
    (identify andThen getData andThen requireData) { implicit request =>
      val existingChildren = request.userAnswers.get(ChildGroup).getOrElse(Nil)
      val nextIndex = existingChildren.length
      val ua = request.userAnswers
      val list = SummaryListViewModel(
        rows = Seq(
          AddAChildSummary.row(ua)
        ).flatten
      )
      Ok(view(form, mode, list, existingChildren, nextIndex))
    }

  def onSubmit(mode: Mode, index: Int): Action[AnyContent] =
    (identify andThen getData andThen requireData).async { implicit request =>
      val existingChildren = request.userAnswers.get(ChildGroup).getOrElse(Nil)
      val ua = request.userAnswers
      val list = SummaryListViewModel(
        rows = Seq(
          AddAChildSummary.row(ua)
        ).flatten
      )
      form
        .bindFromRequest()
        .fold(
          formWithErrors => {
            Future.successful(
              BadRequest(
                view(formWithErrors, mode, list, existingChildren, index)
              )
            )
          },
          addAnother => {
            if (addAnother) {
              cleanup(request.userAnswers) match {
                case Failure(_) => Future.failed(CannotSaveChildError)
                case Success(value) => {
                  sessionRepository.set(value)
                  Future.successful(
                    Redirect(
                      controllers.benefits.routes.ChildsNameController
                        .onPageLoad(mode = mode, index = index)
                    )
                  )
                }
              }
            } else {
              Future.successful(
                Redirect(
                  controllers.benefits.routes.HomeController.onPageLoad()
                )
              )
            }
          }
        )
    }

  def cleanup(userAnswers: UserAnswers): Try[UserAnswers] = {
    for {
      minusChildPage <- userAnswers.remove(ChildsNamePage(0))
      minusQfdlaPage <- minusChildPage.remove(ChildsBirthDatePage(0))
      minusAllPages <- minusQfdlaPage.remove(DlaRatePage(0))
    } yield minusAllPages
  }
}
