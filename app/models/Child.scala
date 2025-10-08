package models

import play.api.libs.json.{Format, Json}

import java.time.LocalDate

case class Child(
    name: String,
    dateOfBirth: LocalDate,
    qualifiesForDLA: Boolean,
    dlaRate: Option[String]
)

case object Child {
  implicit val format: Format[Child] = Json.format
}
