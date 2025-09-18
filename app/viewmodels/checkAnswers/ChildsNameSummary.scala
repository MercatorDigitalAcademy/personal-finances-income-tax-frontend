package viewmodels.checkAnswers

import controllers.routes
import models.{CheckMode, UserAnswers}
import pages.ChildsNamePage
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object ChildsNameSummary  {

  def row(answers: UserAnswers)(implicit messages: Messages): Option[SummaryListRow] =
    answers.get(ChildsNamePage).map {
      answer =>

        SummaryListRowViewModel(
          key     = "childsName.checkYourAnswersLabel",
          value   = ValueViewModel(HtmlFormat.escape(answer).toString),
          actions = Seq(
            ActionItemViewModel("site.change", routes.ChildsNameController.onPageLoad(CheckMode).url)
              .withVisuallyHiddenText(messages("childsName.change.hidden"))
          )
        )
    }
}
