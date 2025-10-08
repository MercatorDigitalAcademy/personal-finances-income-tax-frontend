package pages.benefits

import pages.QuestionPage
import play.api.libs.json.JsPath

case class ChildsNamePage(index: Int) extends QuestionPage[String] {

  override def path: JsPath = JsPath \ ChildGroup.toString \ index \ toString

  override def toString: String = "name"
}
