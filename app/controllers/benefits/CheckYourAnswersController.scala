package controllers.benefits

import com.google.inject.Inject
import controllers.actions.{
  DataRequiredAction,
  DataRetrievalAction,
  IdentifierAction
}
import pages.benefits._
import play.api.i18n.Lang.logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.{
  ChildsBirthDateSummary,
  ChildsNameSummary,
  DlaRateSummary,
  QualifiesForDlaSummary
}
import viewmodels.govuk.all.SummaryListViewModel
import views.html.CheckYourAnswersView

import scala.concurrent.ExecutionContext

class CheckYourAnswersController @Inject() (
    override val messagesApi: MessagesApi,
    identify: IdentifierAction,
    getData: DataRetrievalAction,
    requireData: DataRequiredAction,
    val controllerComponents: MessagesControllerComponents,
    view: CheckYourAnswersView
)(implicit ec: ExecutionContext)
    extends FrontendBaseController
    with I18nSupport {

  def onPageLoad(index: Int): Action[AnyContent] =
    (identify andThen getData andThen requireData) { implicit request =>

      val ua = request.userAnswers
      val childFromChildren = ua.get(ChildWithIndex(index))

      val child = childFromChildren match {
        case Some(child) => child
        case None        => Redirect(controllers.routes.JourneyRecoveryController.onPageLoad())
      }

      logger.info(s"$ua")
      val list = SummaryListViewModel(
        rows = Seq(
          ChildsNameSummary.row(ua, index),
          ChildsBirthDateSummary.row(ua, index),
          QualifiesForDlaSummary.row(ua, index),
          DlaRateSummary.row(ua, index)
        ).flatten
      )
      Ok(view(list, index))
    }

  def onSubmit(index: Int): Action[AnyContent] =
    (identify andThen getData andThen requireData) { implicit request =>
      val ua = request.userAnswers
      println(s"finalizing child: $ua")
      Redirect(controllers.benefits.routes.AddAChildController.onPageLoad())
    }

}
