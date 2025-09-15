package pages

import models.DoYouHaveAnyChildren
import play.api.libs.json.JsPath

case object DoYouHaveAnyChildrenPage extends QuestionPage[Set[DoYouHaveAnyChildren]] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "DoYouHaveAnyChildren"
}
