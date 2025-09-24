package controllers.benefits

import controllers.actions._
import controllers.routes
import forms.benefits.DlaRateFormProvider
import models.requests.DataRequest
import models.{Child, DlaRate, Mode, UserAnswers}
import navigation.Navigator
import pages._
import pages.benefits.{AddAChildPage, ChildGroup, ChildsBirthDatePage, ChildsNamePage, DlaRatePage, QualifiesForDlaPage}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Result}
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.DlaRateView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class DlaRateController @Inject()(
                                       override val messagesApi: MessagesApi,
                                       sessionRepository: SessionRepository,
                                       navigator: Navigator,
                                       identify: IdentifierAction,
                                       getData: DataRetrievalAction,
                                       requireData: DataRequiredAction,
                                       formProvider: DlaRateFormProvider,
                                       val controllerComponents: MessagesControllerComponents,
                                       view: DlaRateView
                                     )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport{

  val form = formProvider()

  def onPageLoad(mode: Mode, maybeIndex: Option[Int]): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>

      val preparedForm = request.userAnswers.get(DlaRatePage) match {
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
        dlaRate => {
          request.userAnswers.set(DlaRatePage, dlaRate) match {
            case Success(updatedUa) => {
              finalizeChild(updatedUa, mode, maybeIndex, dlaRateOpt = Some(dlaRate))
              Future.successful(Redirect(controllers.benefits.routes.AddAChildController.onPageLoad()))
            }
            case Failure(_) =>
              Future.successful(InternalServerError("Could not save DLA rate"))
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
      rate      <- ua.get(DlaRatePage)
    } yield (name, dob, qualifies, rate)) match {

      case None =>
        Future.successful(Redirect(controllers.benefits.routes.HomeController.onPageLoad()))

      case Some((name, dob, qualifies, rate)) =>
        val child = Child(
          name = name,
          dateOfBirth = dob,
          qualifiesForDla = qualifies,
          dlaRate = dlaRateOpt orElse  Some(rate)
        )

        val existing: List[Child] = ua.get(ChildGroup).getOrElse(Nil)
        val updatedChildren = maybeIndex match {
          case Some(i) if i >= 0 && i < existing.length => existing.updated(i, child)
          case _                                        => existing :+ child
        }

        ua.set(ChildGroup, updatedChildren) match {
          case Success(uaWithChildren) =>
            val cleanedTry: Try[UserAnswers] = uaWithChildren
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
