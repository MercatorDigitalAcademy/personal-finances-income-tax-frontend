package viewmodels.checkAnswers

import controllers.routes
import models.{CheckMode, UserAnswers}
import pages.benefits.IsUserClaimingChbPage
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object IsUserClaimingChbSummary {

  def row(
      answers: UserAnswers
  )(implicit messages: Messages): Option[SummaryListRow] =
    answers.get(IsUserClaimingChbPage).map { answer =>
      val value = if (answer) "site.yes" else "site.no"

      SummaryListRowViewModel(
        key = "isUserClaimingChb.checkYourAnswersLabel",
        value = ValueViewModel(value),
        actions = Seq(
          ActionItemViewModel(
            "site.change",
            controllers.benefits.routes.IsUserClaimingChbController
              .onPageLoad(CheckMode)
              .url
          )
            .withVisuallyHiddenText(messages("isUserClaimingChb.change.hidden"))
        )
      )
    }
}
