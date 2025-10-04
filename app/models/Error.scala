package models

class Error extends Exception {}
case object CannotSaveChildError extends Error

import play.api.libs.json.{JsValue, Json, OFormat}

case class ApiError(status: Int, body: ApiErrorBody) {

  def toJson: JsValue = body match {
    case error: SingleErrorBody => Json.toJson(error)
    case errors: MultiErrorsBody => Json.toJson(errors)
  }

  def toMessage : String  = body match {
    case error: SingleErrorBody => error.reason
    case errors: MultiErrorsBody => errors.failures.map(_.reason).mkString(", ")
  }
}
sealed trait ApiErrorBody

/** Single Error * */
case class SingleErrorBody(code: String, reason: String) extends ApiErrorBody

/** Multiple Errors * */
case class MultiErrorsBody(failures: Seq[SingleErrorBody]) extends ApiErrorBody

object SingleErrorBody {
  implicit val formats: OFormat[SingleErrorBody] = Json.format[SingleErrorBody]
  val parsingError: SingleErrorBody = SingleErrorBody("PARSING_ERROR", "Error while parsing response from API")
}

object MultiErrorsBody {
  implicit val formats: OFormat[MultiErrorsBody] = Json.format[MultiErrorsBody]
}
