package viewmodels.checkAnswers

import controllers.routes
import models.{CheckMode, UserAnswers}
import pages.benefits.DlaRatePage
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object DlaRateSummary  {

  def row(answers: UserAnswers, index: Int)(implicit messages: Messages): Option[SummaryListRow] =
    answers.get(DlaRatePage(index)).map {
      answer =>

        val value = ValueViewModel(
          HtmlContent(
            HtmlFormat.escape(messages(s"dlaRate.$answer"))
          )
        )

        SummaryListRowViewModel(
          key     = KeyViewModel("dlaRate.checkYourAnswersLabel"),
          value   = ValueViewModel(value.content),
          actions = Seq(
            ActionItemViewModel("site.change", controllers.benefits.routes.DlaRateController.onPageLoad(index).url)
              .withVisuallyHiddenText(messages("dlaRate.change.hidden"))
          )
        )
    }
}
