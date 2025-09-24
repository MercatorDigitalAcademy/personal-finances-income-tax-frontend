package pages.benefits

import models.DlaRate
import pages.QuestionPage
import play.api.libs.json.JsPath

case object DlaRatePage extends QuestionPage[DlaRate] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "dlaRate"
}
