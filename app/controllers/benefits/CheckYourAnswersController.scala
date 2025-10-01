package controllers.benefits

import com.google.inject.Inject
import controllers.actions.{
  DataRequiredAction,
  DataRetrievalAction,
  IdentifierAction
}
import models.requests.DataRequest
import models.{Child, DlaRate, NormalMode, UserAnswers}
import navigation.Navigator
import pages.benefits._
import play.api.i18n.Lang.logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Result}
import repositories.SessionRepository
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryList
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import viewmodels.checkAnswers.{
  ChildsBirthDateSummary,
  ChildsNameSummary,
  DlaRateSummary,
  QualifiesForDlaSummary
}
import viewmodels.govuk.all.SummaryListViewModel
import views.html.CheckYourAnswersView

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class CheckYourAnswersController @Inject() (
    override val messagesApi: MessagesApi,
    identify: IdentifierAction,
    getData: DataRetrievalAction,
    requireData: DataRequiredAction,
    sessionRepository: SessionRepository,
    navigator: Navigator,
    val controllerComponents: MessagesControllerComponents,
    view: CheckYourAnswersView
)(implicit ec: ExecutionContext)
    extends FrontendBaseController
    with I18nSupport {

  def onPageLoad(index: Int): Action[AnyContent] =
    (identify andThen getData andThen requireData) { implicit request =>
      val children = request.userAnswers.get(ChildGroup).getOrElse(Nil)

      val ua = request.userAnswers

      val nameRow: SummaryList = SummaryListViewModel(
        rows = Seq(ChildsNameSummary.row(ua, index)).flatten
      )

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
      finalizeChild(ua, index)
      Redirect(controllers.benefits.routes.AddAChildController.onPageLoad())
    }

  private def finalizeChild(
      ua: UserAnswers,
      index: Int
  )(implicit request: DataRequest[AnyContent]): Future[Result] = {

    (for {
      name <- ua.get(ChildsNamePage(index))
      dob <- ua.get(ChildsBirthDatePage(index))
      qualifies <- ua.get(QualifiesForDlaPage(index))
    } yield (name, dob, qualifies)) match {

      case None =>
        Future.successful(
          Redirect(controllers.benefits.routes.HomeController.onPageLoad())
        )

      case Some((name, dob, qualifies)) =>
        val maybeRate: Option[DlaRate] = ua.get(DlaRatePage(index))

        val child = Child(
          name = name,
          dateOfBirth = dob,
          qualifiesForDla = qualifies,
          dlaRate = maybeRate
        )

        val existing: List[Child] = ua.get(ChildGroup).getOrElse(Nil)
        val updatedChildren = index match {
          case i if i >= 0 && i < existing.length => existing.updated(i, child)
          case _                                  => existing :+ child
        }
        ua.set(ChildGroup, updatedChildren) match {
          case Success(uaWithChildren) =>
            for {
              updatedAnswers <- Future.fromTry(
                request.userAnswers.set(ChildGroup, updatedChildren)
              )
              _ <- sessionRepository.set(updatedAnswers)
            } yield Redirect(
              controllers.benefits.routes.AddAChildController.onPageLoad()
            )

          case Failure(_) =>
            Future.successful(InternalServerError("Could not save child"))
        }

    }
  }

  private def uaCleanup(ua: UserAnswers, index: Int): Unit = {
    val cleanedTry: Try[UserAnswers] = ua
      .remove(ChildsNamePage(index))
      .flatMap(_.remove(ChildsBirthDatePage(index)))
      .flatMap(_.remove(QualifiesForDlaPage(index)))
      .flatMap(_.remove(DlaRatePage(index)))

    cleanedTry match {
      case Success(cleanedUa: UserAnswers) =>
        sessionRepository.set(cleanedUa).map { _ =>
          Redirect(navigator.nextPage(AddAChildPage, NormalMode, cleanedUa))
        }
      case Failure(_) =>
        sessionRepository.set(ua).map { _ =>
          Redirect(navigator.nextPage(AddAChildPage, NormalMode, ua))
        }
    }

  }

}
