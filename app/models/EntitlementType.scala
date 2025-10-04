package models

import play.api.libs.json.{
  Format,
  JsError,
  JsObject,
  JsResult,
  JsString,
  JsValue,
  Json,
  OFormat
}

import java.util.UUID

sealed trait EntitlementType {
  def code: String
  def name: String
}

case class ChildBenefit(
    userId: String,
    children: List[Child],
    amount: Double
) extends EntitlementType {
  override def code: String = "CHB"
  override def name: String = "child-benefit"
}

object ChildBenefit {
  implicit val format: OFormat[ChildBenefit] = Json.format[ChildBenefit]
}

case class DisabilityLivingAllowance(
    child: Child,
    dlaRate: DlaRate,
    amount: Double
) extends EntitlementType {

  override def code: String = "DLA"

  override def name: String = "disability-living-allowance"
}

object DisabilityLivingAllowance {
  implicit val format: OFormat[DisabilityLivingAllowance] =
    Json.format[DisabilityLivingAllowance]
}

object EntitlementType {
  implicit val format: Format[EntitlementType] = new Format[EntitlementType] {
    override def writes(o: EntitlementType): JsValue = o match {
      case chb: ChildBenefit =>
        Json.toJson(chb)(ChildBenefit.format).as[JsObject] +
          ("entitlementName" -> JsString("ChildBenefit"))
      case dla: DisabilityLivingAllowance =>
        Json.toJson(dla)(DisabilityLivingAllowance.format).as[JsObject] +
          ("entitlementName" -> JsString("DisabilityLivingAllowance"))
    }

    override def reads(json: JsValue): JsResult[EntitlementType] =
      (json \ "entitlementName").validate[String].flatMap {
        case "ChildBenefit" =>
          json.validate[ChildBenefit](ChildBenefit.format)
        case "DisabilityLivingAllowance" =>
          json.validate[DisabilityLivingAllowance](
            DisabilityLivingAllowance.format
          )
        case other =>
          JsError(s"Unknown entitlement type: $other")
      }
  }
}
