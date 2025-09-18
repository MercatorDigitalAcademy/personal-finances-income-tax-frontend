package models

import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.radios.RadioItem

sealed trait IsEntitledToDla

object IsEntitledToDla extends Enumerable.Implicits {

  case object Higher extends WithName("higher") with IsEntitledToDla
  case object Middle extends WithName("middle") with IsEntitledToDla
  case object Lower extends WithName("middle") with IsEntitledToDla

  val values: Seq[IsEntitledToDla] = Seq(
    Higher, Middle, Lower
  )

  def options(implicit messages: Messages): Seq[RadioItem] = values.zipWithIndex.map {
    case (value, index) =>
      RadioItem(
        content = Text(messages(s"isEntitledToDla.${value.toString}")),
        value   = Some(value.toString),
        id      = Some(s"value_$index")
      )
  }

  implicit val enumerable: Enumerable[IsEntitledToDla] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
