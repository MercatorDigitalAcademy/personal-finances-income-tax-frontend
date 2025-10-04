package pages.benefits

import pages.QuestionPage
import play.api.libs.json.JsPath

case class DeleteChildPage(index: Int) extends QuestionPage[Boolean] {

  override def path: JsPath = JsPath \ ChildGroup.toString \ index \toString

  override def toString: String = "deleteChild"
}
