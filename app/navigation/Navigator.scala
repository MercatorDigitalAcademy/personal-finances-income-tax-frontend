/*
 * Copyright 2025 HM Revenue & Customs
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

package navigation

import controllers.routes
import models._
import pages._
import play.api.mvc.Call

import javax.inject.{Inject, Singleton}

@Singleton
class Navigator @Inject() () {

  private val normalRoutes: Page => UserAnswers => Call = {
    case WelcomePage           => _ => routes.DevLoginController.showLogin()
    case StartPage             => _ => routes.IsUserClaimingChbController.onPageLoad(NormalMode)
    case IsUserClaimingChbPage => _ => routes.AddAChildController.onPageLoad()

    case AddAChildPage =>
      ua =>
        ua.get(AddAChildPage) match {
          case Some(true)  => routes.ChildsNameController.onPageLoad(NormalMode)
          case Some(false) => routes.CheckYourAnswersController.onPageLoad()
          case None        => routes.HomeController.onPageLoad()  //TODO lan redirect to sessionExpirecontroller when made
        }

    case ChildsNamePage      => _ => routes.ChildsBirthDateController.onPageLoad(NormalMode)
    case ChildsBirthDatePage => _ => routes.QualifiesForDlaController.onPageLoad()
    case QualifiesForDlaPage =>
      ua =>
        ua.get(QualifiesForDlaPage) match {
          case Some(true)  => routes.DlaRateController.onPageLoad()
          case Some(false) => routes.CheckYourAnswersController.onPageLoad()
          case None        => routes.HomeController.onPageLoad()
        }
    case DlaRatePage => _ => routes.AddAChildController.onPageLoad()
    case _                   => _ => routes.AddAChildController.onPageLoad()
  }

  private val checkRouteMap: Page => UserAnswers => Call = {
    case _ => _ => routes.CheckYourAnswersController.onPageLoad()
  }

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers): Call = mode match {
    case NormalMode => normalRoutes(page)(userAnswers)
    case CheckMode  => checkRouteMap(page)(userAnswers)
  }
}
