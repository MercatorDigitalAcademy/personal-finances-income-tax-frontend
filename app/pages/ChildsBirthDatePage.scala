package pages

import java.time.LocalDate

import play.api.libs.json.JsPath

case object ChildsBirthDatePage extends QuestionPage[LocalDate] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "childsBirthDate"
}
