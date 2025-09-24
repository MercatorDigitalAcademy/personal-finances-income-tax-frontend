package pages.benefits

import pages.QuestionPage
import play.api.libs.json.JsPath

import java.time.LocalDate

case object ChildsBirthDatePage extends QuestionPage[LocalDate] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "childsBirthDate"
}
