package connectors

import models.{ApiError, FetchedBenefits}
import play.api.Logging
import play.api.http.Status._
import play.api.libs.json.{JsObject, Json, OWrites}
import uk.gov.hmrc.http.{HttpReads, HttpResponse}

final case class BenefitsResponse(
    httpResponse: HttpResponse,
    result: Either[ApiError, FetchedBenefits]
)

object BenefitsResponse {

  implicit val benefitsResponseReads: HttpReads[BenefitsResponse] =
    new HttpReads[BenefitsResponse] with Parser with Logging {
      override protected[connectors] val parserName: String = this.getClass.getSimpleName

      override def read(method: String, url: String, response: HttpResponse): BenefitsResponse =
        response.status match {
          case OK => BenefitsResponse(response, extractResult(response))
          case NOT_FOUND | INTERNAL_SERVER_ERROR | SERVICE_UNAVAILABLE | BAD_REQUEST =>
            BenefitsResponse(response, handleError(response, response.status))
          case _ =>
            BenefitsResponse(response, handleError(response, INTERNAL_SERVER_ERROR))
        }

      private def extractResult(response: HttpResponse): Either[ApiError, FetchedBenefits] =
        response.json.validate[FetchedBenefits].fold[Either[ApiError, FetchedBenefits]](
          e => {
            logger.error(s"[BenefitsResponse][extractResult]: Error parsing benefits submission JSON: $e")
            badSuccessJsonResponse
          },
          parsedModel => Right(parsedModel)
        )
    }

  implicit val benefitsResponseWrites: OWrites[BenefitsResponse] = new OWrites[BenefitsResponse] {
    def writes(resp: BenefitsResponse): JsObject = Json.obj(
      "status" -> resp.httpResponse.status,
      "body" -> resp.httpResponse.body,
      "result" -> (resp.result match {
        case Left(err)  => Json.obj("error" -> Json.toJson(err.toJson))
        case Right(data) => Json.obj("data" -> Json.toJson(data))
      })
    )
  }
}