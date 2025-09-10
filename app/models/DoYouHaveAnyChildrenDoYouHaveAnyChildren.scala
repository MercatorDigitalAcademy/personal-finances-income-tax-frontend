package models

import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.checkboxes.CheckboxItem
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import viewmodels.govuk.checkbox._

sealed trait DoYouHaveAnyChildrenDoYouHaveAnyChildren

object DoYouHaveAnyChildrenDoYouHaveAnyChildren extends Enumerable.Implicits {

  case object Yes extends WithName("yes") with DoYouHaveAnyChildrenDoYouHaveAnyChildren
  case object No extends WithName("no") with DoYouHaveAnyChildrenDoYouHaveAnyChildren

  val values: Seq[DoYouHaveAnyChildrenDoYouHaveAnyChildren] = Seq(
    Yes,
    No
  )

  def checkboxItems(implicit messages: Messages): Seq[CheckboxItem] =
    values.zipWithIndex.map {
      case (value, index) =>
        CheckboxItemViewModel(
          content = Text(messages(s"doYouHaveAnyChildrenDoYouHaveAnyChildren.${value.toString}")),
          fieldId = "value",
          index   = index,
          value   = value.toString
        )
    }

  implicit val enumerable: Enumerable[DoYouHaveAnyChildrenDoYouHaveAnyChildren] =
    Enumerable(values.map(v => v.toString -> v): _*)

}
