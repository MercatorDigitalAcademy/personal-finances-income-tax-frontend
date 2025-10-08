package controllers.benefits

import controllers.actions._
import forms.benefits.SubmitChildrenFormProvider
import models.Mode
import pages.benefits.ChildGroup
import play.api.i18n.Lang.logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import services.SubmitChildrenService
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.AddAChildSummary
import viewmodels.govuk.all.SummaryListViewModel
import views.html.benefits.SubmitChildrenView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class SubmitChildrenController @Inject() (
    override val messagesApi: MessagesApi,
    service: SubmitChildrenService,
    identify: IdentifierAction,
    getData: DataRetrievalAction,
    requireData: DataRequiredAction,
    formProvider: SubmitChildrenFormProvider,
    val controllerComponents: MessagesControllerComponents,
    view: SubmitChildrenView
)(implicit ec: ExecutionContext)
    extends FrontendBaseController
    with I18nSupport {

  val form = formProvider()

  def onPageLoad(mode: Mode): Action[AnyContent] =
    (identify andThen getData andThen requireData) { implicit request =>
      val children = request.userAnswers.get(ChildGroup).getOrElse(Nil)
      logger.info(
        s"[SubmitChildren][onPageLoad] existing child List: $children"
      )
      val ua = request.userAnswers
      val list = SummaryListViewModel(
        rows = Seq(
          AddAChildSummary.row(ua)
        ).flatten
      )
      Ok(view(form, mode, list, children))
    }

  def onSubmit(mode: Mode): Action[AnyContent] =
    (identify andThen getData andThen requireData).async { implicit request =>
      val userId = request.userId
      val children = request.userAnswers.get(ChildGroup).getOrElse(Nil)
      val ua = request.userAnswers
      val list = SummaryListViewModel(
        rows = Seq(
          AddAChildSummary.row(ua)
        ).flatten
      )

      form
        .bindFromRequest()
        .fold(
          formWithErrors =>
            Future.successful(
              BadRequest(view(formWithErrors, mode, list, children))
            ),
          value =>
            if (value) {
              logger.info(s"[SubmitChildrenController]submitting to BenefitsService")
              service.submitChildren(userId, children).map {
                case Left(error) => {
                  logger.info("[SubmitChildrenController]receiving service error from benefitService")
                  Redirect(controllers.routes.JourneyRecoveryController.onPageLoad())
                }
                case Right(_) => {
                  logger.info("[SubmitChildrenController]receiving Right from benefitService")
                  Redirect(controllers.benefits.routes.IndexController.onPageLoad(userId))
                }
              }
            } else {
              Future.successful(Redirect(controllers.routes.JourneyRecoveryController.onPageLoad()))
            }
        )
    }
}
