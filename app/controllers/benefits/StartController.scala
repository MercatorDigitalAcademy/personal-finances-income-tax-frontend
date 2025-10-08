package controllers.benefits

import controllers.actions._
import models.UserAnswers
import play.api.i18n.Lang.logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.StartView

import javax.inject.Inject

class StartController @Inject() (
    override val messagesApi: MessagesApi,
    identify: IdentifierAction,
    getData: DataRetrievalAction,
    requireData: DataRequiredAction,
    sessionRepository: SessionRepository,
    val controllerComponents: MessagesControllerComponents,
    view: StartView
) extends FrontendBaseController
    with I18nSupport {

  def onPageLoad(): Action[AnyContent] = (identify andThen getData) { implicit request =>
    sessionRepository.set(UserAnswers(request.userId))

    logger.info(s"Session: ${request.session}")
    Ok(view())
  }

}
