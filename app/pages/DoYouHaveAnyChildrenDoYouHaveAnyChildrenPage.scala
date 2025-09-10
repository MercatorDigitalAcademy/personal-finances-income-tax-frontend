package pages

import models.DoYouHaveAnyChildrenDoYouHaveAnyChildren
import play.api.libs.json.JsPath

case object DoYouHaveAnyChildrenDoYouHaveAnyChildrenPage extends QuestionPage[Set[DoYouHaveAnyChildrenDoYouHaveAnyChildren]] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "doYouHaveAnyChildren"
}
