package viewmodels.checkAnswers

import controllers.routes
import models.{CheckMode, UserAnswers}
import pages.IsEntitledToDlaPage
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object IsEntitledToDlaSummary  {

  def row(answers: UserAnswers)(implicit messages: Messages): Option[SummaryListRow] =
    answers.get(IsEntitledToDlaPage).map {
      answer =>

        val value = ValueViewModel(
          HtmlContent(
            HtmlFormat.escape(messages(s"isEntitledToDla.$answer"))
          )
        )

        SummaryListRowViewModel(
          key     = "isEntitledToDla.checkYourAnswersLabel",
          value   = value,
          actions = Seq(
            ActionItemViewModel("site.change", routes.IsEntitledToDlaController.onPageLoad(CheckMode).url)
              .withVisuallyHiddenText(messages("isEntitledToDla.change.hidden"))
          )
        )
    }
}
