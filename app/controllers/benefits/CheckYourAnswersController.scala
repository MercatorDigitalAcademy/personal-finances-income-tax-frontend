package controllers.benefits

import com.google.inject.Inject
import controllers.actions.{
  DataRequiredAction,
  DataRetrievalAction,
  IdentifierAction
}
import pages.benefits.ChildGroup
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.AddAChildSummary
import viewmodels.govuk.all.SummaryListViewModel
import views.html.CheckYourAnswersView

class CheckYourAnswersController @Inject() (
  override val messagesApi: MessagesApi,
  identify: IdentifierAction,
  getData: DataRetrievalAction,
  requireData: DataRequiredAction,
  val controllerComponents: MessagesControllerComponents,
  view: CheckYourAnswersView
) extends FrontendBaseController
    with I18nSupport {

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>

    val children = request.userAnswers.get(ChildGroup).getOrElse(Nil)

    val ua = request.userAnswers

    val list = SummaryListViewModel(
      rows = Seq(
        AddAChildSummary.row(ua)
      ).flatten
    )

    Ok(view(list))
  }
}
