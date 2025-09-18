package viewmodels.checkAnswers

import controllers.routes
import models.{CheckMode, UserAnswers}
import pages.ChildsBirthDatePage
import play.api.i18n.{Lang, Messages}
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import utils.DateTimeFormats.dateTimeFormat
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object ChildsBirthDateSummary  {

  def row(answers: UserAnswers)(implicit messages: Messages): Option[SummaryListRow] =
    answers.get(ChildsBirthDatePage).map {
      answer =>

        implicit val lang: Lang = messages.lang

        SummaryListRowViewModel(
          key     = "childsBirthDate.checkYourAnswersLabel",
          value   = ValueViewModel(answer.format(dateTimeFormat())),
          actions = Seq(
            ActionItemViewModel("site.change", routes.ChildsBirthDateController.onPageLoad(CheckMode).url)
              .withVisuallyHiddenText(messages("childsBirthDate.change.hidden"))
          )
        )
    }
}
