/*
package viewmodels.checkAnswers

import controllers.routes
import models.{CheckMode, UserAnswers}
import pages.benefits.DeleteChildPage
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object DeleteChildSummary  {

  def row(answers: UserAnswers, index: Int)(implicit messages: Messages): Option[SummaryListRow] =
    answers.get(DeleteChildPage(index)).map {
      answer =>

        val value = if (answer) "site.yes" else "site.no"

        SummaryListRowViewModel(
          key     = "deleteChild.checkYourAnswersLabel",
          value   = ValueViewModel(value),
          actions = Seq(
            ActionItemViewModel("site.change", controllers.benefits.routes.DeleteChildController.onPageLoad(index = index, ).url)
              .withVisuallyHiddenText(messages("deleteChild.change.hidden"))
          )
        )
    }
}
*/
