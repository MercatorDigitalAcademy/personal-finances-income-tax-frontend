package models

import play.api.libs.json.{Format, Json}

import java.time.LocalDate

case class Child(
                  name: String,
                  dateOfBirth: LocalDate,
                  qualifiesForDla: Boolean,
                  dlaRate: Option[DlaRate]
                )

case object Child {
  implicit val format: Format[Child] = Json.format
}

