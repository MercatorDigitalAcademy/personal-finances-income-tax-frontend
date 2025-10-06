/*
 * Copyright 2024 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package connectors

import config.FrontendAppConfig
import models.{ApiError, Child, FetchedBenefits}
import play.api.Logging
import play.api.http.Status._
import play.api.libs.json.{JsString, Json}
import uk.gov.hmrc.http.client.HttpClientV2
import uk.gov.hmrc.http.{HeaderCarrier, HttpReads, HttpResponse, StringContextOps}

import java.util.UUID
import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

case class UpdateStatusResponse(
    httpResponse: HttpResponse,
    result: Either[ApiError, String]
)

class BenefitsConnector @Inject() (
    httpClient: HttpClientV2,
    appConfig: FrontendAppConfig
)(implicit
    ec: ExecutionContext
) extends Logging {

  def benefitsSubmission(userId: UUID, children: List[Child])(implicit
      hc: HeaderCarrier
  ): Future[Either[ApiError, FetchedBenefits]] = {
    val getBenefitsUrl =
      s"${appConfig.benefitsServiceBaseUrl}"

    httpClient
      .put(url"$getBenefitsUrl")
      .setHeader("userId" -> userId.toString)
      .withBody(Json.obj("children" -> JsString(children.toString())))
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
