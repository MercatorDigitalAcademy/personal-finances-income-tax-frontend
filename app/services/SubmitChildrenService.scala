package services

import connectors.BenefitsConnector
import models.{ApiError, Child, FetchedBenefits, HttpParserError, ServiceError}
import play.api.Logging
import uk.gov.hmrc.http.HeaderCarrier

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class SubmitChildrenService @Inject() (
    connector: BenefitsConnector
)(implicit
    val ec: ExecutionContext
) extends Logging {

  def submitChildren(userId: String, children: List[Child])(implicit
      hc: HeaderCarrier
  ): Future[Either[ServiceError, FetchedBenefits]] = {
    logger.info(s"[SubmitChildrenService][submitChildren]receiving child list $children")
    connector.benefitsSubmission(userId, children).map {
      case Left(error)     => {
        logger.info("[SubmitChildrenService][submitChildren] Receiving Left from connector")
        Left(HttpParserError(error.status))
      }
      case Right(benefits) => {
        logger.info("[SubmitChildrenService][submitChildren] Receiving Left from connector")
        Right(benefits)
      }
    }
  }

  def getEntitlementsByUser(userId: String)(implicit hc: HeaderCarrier): Future[Either[ApiError, FetchedBenefits]] = {
    connector.getBenefitsByUser(userId).map {
      case Left(error) => Left(error)
      case Right(value) => Right(value)
    }
  }

}
