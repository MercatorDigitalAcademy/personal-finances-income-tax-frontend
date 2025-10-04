package controllers.benefits

import controllers.actions._
import forms.benefits.DeleteChildFormProvider
import models.{CheckMode, Child, Mode}
import navigation.Navigator
import pages.benefits.{ChildGroup, ChildWithIndex, DeleteChildPage}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.benefits.DeleteChildView

import java.time.LocalDate
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class DeleteChildController @Inject() (
    override val messagesApi: MessagesApi,
    sessionRepository: SessionRepository,
    navigator: Navigator,
    identify: IdentifierAction,
    getData: DataRetrievalAction,
    requireData: DataRequiredAction,
    formProvider: DeleteChildFormProvider,
    val controllerComponents: MessagesControllerComponents,
    view: DeleteChildView
)(implicit ec: ExecutionContext)
    extends FrontendBaseController
    with I18nSupport {

  val form = formProvider()

  def onPageLoad(mode: Mode, index: Int): Action[AnyContent] =
    (identify andThen getData andThen requireData) { implicit request =>
      val child = request.userAnswers.get(ChildWithIndex(index)).map(child => child).getOrElse(Child(childsName = "???", childsBirthDate = LocalDate.of(2018,1,2), qualifiesForDla = false, dlaRate = None))

      Ok(view(form, mode, index, child))
    }

  def onSubmit(mode: Mode, index: Int): Action[AnyContent] =
    (identify andThen getData andThen requireData).async { implicit request =>
      val child = request.userAnswers.get(ChildWithIndex(index)).map(child => child).getOrElse(Child(childsName = "???", childsBirthDate = LocalDate.of(2018,1,2), qualifiesForDla = false, dlaRate = None))

      form
        .bindFromRequest()
        .fold(
          formWithErrors =>
            Future.successful(
              BadRequest(view(formWithErrors, mode, index, child))
            ),
          value => {
            if (value) {
              val childList: List[Child] = request.userAnswers.get(ChildGroup) match {
                case Some(value) => value
                case None => List.empty
              }
              for {
                deletedChild <- deleteChild(childList, index)
                newChildGroup <-  Future.fromTry(request.userAnswers.set(ChildGroup, deletedChild))
                _ <- sessionRepository.set(newChildGroup)
              } yield Redirect(
                navigator.nextPage(
                  page = DeleteChildPage(index),
                  mode = CheckMode,
                  index = index,
                  userAnswers = newChildGroup
                )
              )
            } else Future.successful(Redirect(
              navigator.nextPage(
                page = DeleteChildPage(index),
                mode = CheckMode,
                index = index,
                userAnswers = request.userAnswers
              )
            ))
          }

        )
    }

  private def deleteChild(childList: List[Child], index: Int): Future[List[Child]] = {
    Future(childList.patch(index, Nil, 1))
  }

}
