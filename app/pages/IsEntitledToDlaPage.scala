package pages

import models.IsEntitledToDla
import play.api.libs.json.JsPath

case object IsEntitledToDlaPage extends QuestionPage[IsEntitledToDla] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "isEntitledToDla"
}
