package controllers.benefits

import controllers.actions._
import controllers.routes
import forms.benefits.QualifiesForDlaFormProvider
import models.requests.DataRequest
import models.{Child, DlaRate, Mode, UserAnswers}
import navigation.Navigator
import pages._
import pages.benefits.{AddAChildPage, ChildGroup, ChildsBirthDatePage, ChildsNamePage, DlaRatePage, QualifiesForDlaPage}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Result}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.QualifiesForDlaView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class QualifiesForDlaController @Inject()(
                                         override val messagesApi: MessagesApi,
                                         sessionRepository: SessionRepository,
                                         navigator: Navigator,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         formProvider: QualifiesForDlaFormProvider,
                                         val controllerComponents: MessagesControllerComponents,
                                         view: QualifiesForDlaView
                                 )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  val form = formProvider()

  def onPageLoad(mode: Mode, maybeIndex: Option[Int]): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>

      val preparedForm = request.userAnswers.get(QualifiesForDlaPage) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      Ok(view(preparedForm, mode))
  }

  def onSubmit(mode: Mode, maybeIndex: Option[Int]): Action[AnyContent] =
    (identify andThen getData andThen requireData).async { implicit request =>
      form.bindFromRequest().fold(
        formWithErrors =>
          Future.successful(BadRequest(view(formWithErrors, mode))),
        qualifies => {
          request.userAnswers.set(QualifiesForDlaPage, qualifies) match {
            case scala.util.Success(updatedUa) =>
              if (qualifies) {
                sessionRepository.set(updatedUa).map { _ =>
                  Redirect(navigator.nextPage(QualifiesForDlaPage, mode, updatedUa))
                }
              } else {
                finalizeChild(updatedUa, mode, maybeIndex, dlaRateOpt = None)
                Future.successful(Redirect(controllers.benefits.routes.AddAChildController.onPageLoad()))
              }

            case scala.util.Failure(_) =>
              Future.successful(InternalServerError("Could not save answer"))
          }
        }
      )
    }

  private def finalizeChild(
                             ua: UserAnswers,
                             mode: Mode,
                             maybeIndex: Option[Int],
                             dlaRateOpt: Option[DlaRate]
                           )(implicit request: DataRequest[AnyContent]): Future[Result] = {

    (for {
      name      <- ua.get(ChildsNamePage)
      dob       <- ua.get(ChildsBirthDatePage)
      qualifies <- ua.get(QualifiesForDlaPage)
    } yield (name, dob, qualifies)) match {

      case None =>
        Future.successful(Redirect(controllers.benefits.routes.HomeController.onPageLoad()))

      case Some((name, dob, qualifies)) =>
        val child = Child(
          name = name,
          dateOfBirth = dob,
          qualifiesForDla = qualifies,
          dlaRate = dlaRateOpt
        )

        val existing: List[Child] = ua.get(ChildGroup).getOrElse(Nil)
        val updatedChildren = maybeIndex match {
          case Some(i) if i >= 0 && i < existing.length => existing.updated(i, child)
          case _                                        => existing :+ child
        }

        ua.set(ChildGroup, updatedChildren) match {
          case Success(uaWithChildren) =>
            val cleanedTry = uaWithChildren
              .remove(ChildsNamePage)
              .flatMap(_.remove(ChildsBirthDatePage))
              .flatMap(_.remove(QualifiesForDlaPage))
              .flatMap(_.remove(DlaRatePage))

            cleanedTry match {
              case Success(cleanedUa: UserAnswers) =>
                sessionRepository.set(cleanedUa).map { _ =>
                  Redirect(navigator.nextPage(AddAChildPage, mode, cleanedUa))
                }
              case Failure(_) =>
                sessionRepository.set(uaWithChildren).map { _ =>
                  Redirect(navigator.nextPage(AddAChildPage, mode, uaWithChildren))
                }
            }

          case Failure(_) =>
            Future.successful(InternalServerError("Could not save child"))
        }
    }
  }

}
