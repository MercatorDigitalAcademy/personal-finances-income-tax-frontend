package controllers.actions

import models.requests.IdentifierRequest
import play.api.mvc._

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class FakeIdentifierAction @Inject() (
                                       val bodyParsers: BodyParsers.Default
                                     )(implicit val executionContext: ExecutionContext)
  extends IdentifierAction {

  override def parser: BodyParsers.Default = bodyParsers

  override def invokeBlock[A](
                               request: Request[A],
                               block: IdentifierRequest[A] => Future[Result]
                             ): Future[Result] = {
    request.session.get("userId") match {
      case Some(userId) =>
        block(IdentifierRequest(request, userId))
      case None =>
        Future.successful(
          Results.Redirect(controllers.routes.LogoutController.onPageLoad)
        )
    }
  }
}
