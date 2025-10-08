package connectors

import config.FrontendAppConfig
import models.{ApiError, Child, FetchedBenefits}
import play.api.Logging
import play.api.libs.json.{JsString, Json}
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse, StringContextOps}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class BenefitsConnector @Inject() (
    httpClient: HttpClientV2,
    appConfig: FrontendAppConfig
)(implicit
    ec: ExecutionContext
) extends Logging {

  def benefitsSubmission(userId: String, children: List[Child])(implicit
      hc: HeaderCarrier
  ): Future[Either[ApiError, FetchedBenefits]] = {
    val getBenefitsUrl =
      s"${appConfig.postBenefits}"
    val bodyJson = Json.obj("children" -> Json.toJson(children))
    println("Bodyjson:" + bodyJson)
    logger.info(
      s"[BenefitsConnector][benefitsSubmission] Sending POST to $getBenefitsUrl with body: $bodyJson and userId: $userId"
    )
    httpClient
      .post(url"$getBenefitsUrl")
      .setHeader("userId" -> userId)
      .withBody(bodyJson)
      .execute[BenefitsResponse]
      .map { response =>
        println("Response received from Benefits API" + response)
        if (response.result.isLeft) {
          logger.error(
            s"Error getting entitlements" +
              s" status: ${response.httpResponse.status}; Body:${response.httpResponse.body}"
          )
        }
        response.result
      }
  }
  def getBenefitsByUser(
      userId: String
  )(implicit hc: HeaderCarrier): Future[Either[ApiError, FetchedBenefits]] = {
    val getBenefitsUrl =
      s"${appConfig.getBenefitsForUser}"
    httpClient
      .get(url"$getBenefitsUrl")
      .setHeader("userId" -> userId.toString)
      .execute[BenefitsResponse]
      .map { response: BenefitsResponse =>
        println("Response received from Benefits API" + BenefitsResponse)
        if (response.result.isLeft) {
          logger.error(
            s"Error getting entitlements" +
              s" status: ${response.httpResponse.status}; Body:${response.httpResponse.body}"
          )
        }
        response.result
      }
  }
}
