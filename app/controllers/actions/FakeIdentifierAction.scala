package controllers.actions

import models.requests.IdentifierRequest
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class FakeIdentifierAction(parser: BodyParsers.Default)(implicit
    ec: ExecutionContext
) extends IdentifierAction {

  override val executionContext: ExecutionContext = ec
  override def parser: BodyParsers.Default = parser

  override def invokeBlock[A](
      request: Request[A],
      block: IdentifierRequest[A] => Future[Result]
  ): Future[Result] = {
    request.session.get("userId") match {
      case Some(userId) =>
        block(IdentifierRequest(request, userId))
      case None =>
        Future.successful(
          Results.Redirect(controllers.routes.DevLoginController.showLogin)
        )
    }
  }
}
