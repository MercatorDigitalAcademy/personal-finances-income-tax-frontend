package controllers.benefits

import com.google.inject.Inject
import controllers.actions.{DataRequiredAction, DataRetrievalAction, IdentifierAction}
import models.Mode
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.CheckYourAnswersView

import scala.concurrent.ExecutionContext

class EntitlementsForUser @Inject() (
                                      override val messagesApi: MessagesApi,
                                      identify: IdentifierAction,
                                      getData: DataRetrievalAction,
                                      requireData: DataRequiredAction,
                                      val controllerComponents: MessagesControllerComponents,
                                      view: CheckYourAnswersView
                                    )(implicit ec: ExecutionContext)
  extends FrontendBaseController
    with I18nSupport {

  def onPageLoad(): Action[AnyContent] =
    (identify andThen getData andThen requireData) { implicit request =>
        ???
    }


}
