package viewmodels.checkAnswers

import config.CurrencyFormatter.currencyFormat
import controllers.routes
import models.{CheckMode, UserAnswers}
import pages.AnnualIncomePage
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object AnnualIncomeSummary  {

  def row(answers: UserAnswers)(implicit messages: Messages): Option[SummaryListRow] =
    answers.get(AnnualIncomePage).map {
      answer =>

        SummaryListRowViewModel(
          key     = "annualIncome.checkYourAnswersLabel",
          value   = ValueViewModel(currencyFormat(answer)),
          actions = Seq(
            ActionItemViewModel("site.change", routes.AnnualIncomeController.onPageLoad().url)
              .withVisuallyHiddenText(messages("annualIncome.change.hidden"))
          )
        )
    }
}
