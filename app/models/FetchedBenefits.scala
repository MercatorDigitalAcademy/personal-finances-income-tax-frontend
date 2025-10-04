package models


import play.api.libs.json.{Format, Json, OFormat}

import java.time.Instant

final case class FetchedBenefits(
                              entitlements: List[Entitlement],
                            )

object FetchedBenefits {
  implicit val format: OFormat[FetchedBenefits] = Json.format[FetchedBenefits]
}

case class Entitlement(
                        entitlementId: String,
                        userId: String,
                        entitlementType: EntitlementType,
                        amount: Double,
                        createdAt: Option[Instant] = None,
                        updatedAt: Option[Instant] = None
                      )

object Entitlement {
  implicit val entitlementTypeFormat: Format[EntitlementType] =
    EntitlementType.format
  implicit val format: OFormat[Entitlement] = Json.format[Entitlement]
}


