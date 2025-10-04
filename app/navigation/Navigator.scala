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
import pages.benefits.{
  AddAChildPage,
  ChildGroup,
  ChildsBirthDatePage,
  ChildsNamePage,
  DlaRatePage,
  IsUserClaimingChbPage,
  QualifiesForDlaPage,
  StartPage,
  WelcomePage,
  DeleteChildPage
}
import play.api.mvc.Call

import javax.inject.{Inject, Singleton}

@Singleton
class Navigator @Inject() () {

  private val normalRoutes: Page => UserAnswers => Call = {
    case WelcomePage => _ => routes.DevLoginController.showLogin()
    case StartPage =>
      _ =>
        controllers.benefits.routes.IsUserClaimingChbController
          .onPageLoad(NormalMode)
    case IsUserClaimingChbPage =>
      _ => controllers.benefits.routes.AddAChildController.onPageLoad()
    case AddAChildPage =>
      ua =>
        ua.get(AddAChildPage) match {
          case Some(true) =>
            val nextIndex = ua.get(ChildGroup).map(_.length).getOrElse(0)
            controllers.benefits.routes.ChildsNameController
              .onPageLoad(NormalMode, index = nextIndex)
          case Some(false) =>
            controllers.benefits.routes.HomeController.onPageLoad()
          case None => routes.JourneyRecoveryController.onPageLoad()
        }
    case _ => _ => controllers.benefits.routes.HomeController.onPageLoad()
  }

  private val checkRouteMap: Page => UserAnswers => Call = { case _ =>
    _ => controllers.benefits.routes.HomeController.onPageLoad()
  }

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers): Call =
    mode match {
      case NormalMode => normalRoutes(page)(userAnswers)
      case CheckMode  => checkRouteMap(page)(userAnswers)
    }

  def nextPage(
      page: Page,
      mode: Mode,
      userAnswers: UserAnswers,
      index: Int
  ): Call = mode match {
    case NormalMode =>
      withIndexNormalRoutes(page, mode, userAnswers, index)
    case CheckMode =>
      withIndexCheckRoutes(page, mode, userAnswers, index)
  }

  private def withIndexNormalRoutes(
      page: Page,
      mode: Mode,
      userAnswers: UserAnswers,
      index: Int
  ): Call = {
    page match {
      case ChildsNamePage(index) =>
        controllers.benefits.routes.ChildsBirthDateController
          .onPageLoad(NormalMode, index)
      case ChildsBirthDatePage(index) =>
        controllers.benefits.routes.QualifiesForDlaController.onPageLoad(index)
      case QualifiesForDlaPage(index) =>
        qualifiesForDlaNormalRoute(userAnswers: UserAnswers, index: Int)
      case DlaRatePage(index) =>
        controllers.benefits.routes.CheckYourAnswersController.onPageLoad(index)
    }

  }

  private def qualifiesForDlaNormalRoute(
      userAnswers: UserAnswers,
      index: Int
  ): Call = {
    userAnswers.get(QualifiesForDlaPage(index)) match {
      case Some(true) =>
        controllers.benefits.routes.DlaRateController.onPageLoad(index)
      case Some(false) =>
        controllers.benefits.routes.AddAChildController.onPageLoad()
      case None => controllers.benefits.routes.HomeController.onPageLoad()
    }
  }

  private def withIndexCheckRoutes(
      page: Page,
      mode: Mode,
      userAnswers: UserAnswers,
      index: Int
  ): Call = {
    page match {
      case ChildsNamePage(index) =>
        controllers.benefits.routes.CheckYourAnswersController.onPageLoad(index)
      case ChildsBirthDatePage(index) =>
        controllers.benefits.routes.CheckYourAnswersController.onPageLoad(index)
      case QualifiesForDlaPage(index) =>
        qualifiesForDlaNormalRoute(userAnswers: UserAnswers, index: Int)
      case DlaRatePage(index) =>
        controllers.benefits.routes.CheckYourAnswersController.onPageLoad(index)
      case DeleteChildPage(index) => controllers.benefits.routes.AddAChildController.onPageLoad()
    }
  }
}
