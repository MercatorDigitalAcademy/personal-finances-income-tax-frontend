package viewmodels.checkAnswers

import models.UserAnswers
import pages.benefits.AddAChildPage
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object AddAChildSummary {
  // TODO lan fix or remove

  def row(
      answers: UserAnswers
  )(implicit messages: Messages): Option[SummaryListRow] =
    answers.get(AddAChildPage).map { answer =>
      val value = if (answer) "site.yes" else "site.no"

      SummaryListRowViewModel(
        key = KeyViewModel("addAChild.checkYourAnswersLabel"),
        value = ValueViewModel(value),
        actions = Seq(
          ActionItemViewModel(
            "site.change",
            controllers.benefits.routes.AddAChildController.onPageLoad().url
          )
            .withVisuallyHiddenText(messages("addAChild.change.hidden")),
          ActionItemViewModel(
            "site.remove",
            controllers.benefits.routes.HomeController.onPageLoad().url
          )
            .withVisuallyHiddenText(messages("esbaClaimAmount.change.hidden"))
        ),
        actionsCss = "w-25"
        )

    }
}
