package models

import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.checkboxes.CheckboxItem
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import viewmodels.govuk.checkbox._

sealed trait DoYouHaveAnyChildren

object DoYouHaveAnyChildren extends Enumerable.Implicits {

  case object Yes extends WithName("yes") with DoYouHaveAnyChildren
  case object No extends WithName("no") with DoYouHaveAnyChildren

  val values: Seq[DoYouHaveAnyChildren] = Seq(
    Yes,
    No
  )

  def checkboxItems(implicit messages: Messages): Seq[CheckboxItem] =
    values.zipWithIndex.map {
      case (value, index) =>
        CheckboxItemViewModel(
          content = Text(messages(s"doYouHaveAnyChildren.${value.toString}")),
          fieldId = "value",
          index   = index,
          value   = value.toString
        )
    }

  implicit val enumerable: Enumerable[DoYouHaveAnyChildren] =
    Enumerable(values.map(v => v.toString -> v): _*)

}
