package pages.benefits

import pages.QuestionPage
import play.api.libs.json.JsPath

case object QualifiesForDlaPage extends QuestionPage[Boolean] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "qualifiesForDla"
}
