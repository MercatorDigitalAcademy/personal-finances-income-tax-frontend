package viewmodels.checkAnswers

import controllers.routes
import models.{CheckMode, UserAnswers}
import pages.benefits.QualifiesForDlaPage
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object QualifiesForDlaSummary {

  def row(answers: UserAnswers, index: Int)(implicit
      messages: Messages
  ): Option[SummaryListRow] =
    answers.get(QualifiesForDlaPage(index)).map { answer =>
      val value = if (answer) "site.yes" else "site.no"

      SummaryListRowViewModel(
        key = "qualifiesForDla.checkYourAnswersLabel",
        value = ValueViewModel(value),
        actions = Seq(
          ActionItemViewModel(
            "site.change",
            controllers.benefits.routes.QualifiesForDlaController
              .onPageLoad(index)
              .url
          )
            .withVisuallyHiddenText(messages("qualifiesForDla.change.hidden"))
        )
      )
    }
}
