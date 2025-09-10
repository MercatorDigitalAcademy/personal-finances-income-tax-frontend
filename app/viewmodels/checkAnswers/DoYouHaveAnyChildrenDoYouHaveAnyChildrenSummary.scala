package viewmodels.checkAnswers

import controllers.routes
import models.{CheckMode, UserAnswers}
import pages.DoYouHaveAnyChildrenDoYouHaveAnyChildrenPage
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.govuk.summarylist._
import viewmodels.implicits._

object DoYouHaveAnyChildrenDoYouHaveAnyChildrenSummary  {

  def row(answers: UserAnswers)(implicit messages: Messages): Option[SummaryListRow] =
    answers.get(DoYouHaveAnyChildrenDoYouHaveAnyChildrenPage).map {
      answers =>

        val value = ValueViewModel(
          HtmlContent(
            answers.map {
              answer => HtmlFormat.escape(messages(s"doYouHaveAnyChildrenDoYouHaveAnyChildren.$answer")).toString
            }
            .mkString(",<br>")
          )
        )

        SummaryListRowViewModel(
          key     = "doYouHaveAnyChildrenDoYouHaveAnyChildren.checkYourAnswersLabel",
          value   = value,
          actions = Seq(
            ActionItemViewModel("site.change", routes.DoYouHaveAnyChildrenDoYouHaveAnyChildrenController.onPageLoad(CheckMode).url)
              .withVisuallyHiddenText(messages("doYouHaveAnyChildrenDoYouHaveAnyChildren.change.hidden"))
          )
        )
    }
}
