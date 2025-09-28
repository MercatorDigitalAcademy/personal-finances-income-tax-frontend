package viewmodels.checkAnswers

import controllers.routes
import models.{CheckMode, UserAnswers}
import pages.benefits.ChildsNamePage
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object ChildsNameSummary  {

  def row(answers: UserAnswers, index: Int)(implicit messages: Messages): Option[SummaryListRow] =
    answers.get(ChildsNamePage(index)).map {
      answer =>

        SummaryListRowViewModel(
          key     = "childsName.checkYourAnswersLabel",
          value   = ValueViewModel(HtmlFormat.escape(answer).toString),
          actions = Seq(
            ActionItemViewModel("site.change", controllers.benefits.routes.ChildsNameController.onPageLoad(CheckMode, index).url)
              .withVisuallyHiddenText(messages("childsName.change.hidden"))
          )
        )
    }
}
