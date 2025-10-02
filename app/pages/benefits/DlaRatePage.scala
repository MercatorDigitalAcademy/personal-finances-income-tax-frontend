package pages.benefits

import models.DlaRate
import pages.QuestionPage
import play.api.libs.json.JsPath

case class DlaRatePage(index: Int) extends QuestionPage[DlaRate] {

  override def path: JsPath = JsPath \ ChildGroup.toString \ index \toString

  override def toString: String = "dlaRate"
}
