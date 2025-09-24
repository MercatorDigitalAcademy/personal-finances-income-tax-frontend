package pages.benefits

import pages.QuestionPage
import play.api.libs.json.JsPath

case object ChildsNamePage extends QuestionPage[String] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "childsName"
}
