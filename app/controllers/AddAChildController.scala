package controllers

import controllers.actions._
import forms.AddAChildFormProvider
import models.{Child, Mode, NormalMode}
import navigation.Navigator
import pages.{AddAChildPage, ChildGroup}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.AddAChildSummary
import viewmodels.govuk.all.SummaryListViewModel
import views.html.AddAChildView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class AddAChildController @Inject()(
                                     identify: IdentifierAction,
                                     getData: DataRetrievalAction,
                                     requireData: DataRequiredAction,
                                     sessionRepository: SessionRepository,
                                     formProvider: AddAChildFormProvider,
                                     val controllerComponents: MessagesControllerComponents,
                                     view: AddAChildView
                                   )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  private val form = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    val existingChildren = request.userAnswers.get(ChildGroup).getOrElse(Nil)
    val ua = request.userAnswers
    val list = SummaryListViewModel(
      rows = Seq(
        AddAChildSummary.row(ua)
      ).flatten
    )
    Ok(view(form, mode, list, existingChildren))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors => {
        val existingChildren = request.userAnswers.get(ChildGroup).getOrElse(Nil)
        val ua = request.userAnswers
        val list = SummaryListViewModel(
          rows = Seq(
            AddAChildSummary.row(ua)
          ).flatten
        )
        Future.successful(BadRequest(view(formWithErrors, mode, list, existingChildren)))
      },
      addAnother => {
        if (addAnother) {
          // go to ChildNamePage (start adding a new child)
          Future.successful(Redirect(routes.ChildsNameController.onPageLoad(mode)))
        } else {
          // straight to Check Your Answers
          Future.successful(Redirect(routes.CheckYourAnswersController.onPageLoad()))
        }
      }
    )
  }
}
