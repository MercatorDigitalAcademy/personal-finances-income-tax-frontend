package models

import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.radios.RadioItem

sealed trait DlaRate

object DlaRate extends Enumerable.Implicits {

  case object Higher extends WithName("higher") with DlaRate
  case object Middle extends WithName("middle") with DlaRate
  case object Lower extends WithName("middle") with DlaRate

  val values: Seq[DlaRate] = Seq(
    Higher, Middle, Lower
  )

  def options(implicit messages: Messages): Seq[RadioItem] = values.zipWithIndex.map {
    case (value, index) =>
      RadioItem(
        content = Text(messages(s"dlaRate.${value.toString}")),
        value   = Some(value.toString),
        id      = Some(s"value_$index")
      )
  }

  implicit val enumerable: Enumerable[DlaRate] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
