package pages.benefits

import models.UserAnswers
import pages._
import play.api.libs.json.JsPath

import scala.util.Try

case class AddAChildPage(nextIndex: Int)

case object AddAChildPage extends QuestionPage[Boolean] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "addAChild"

  def cleanup(userAnswers: UserAnswers, index: Int): Try[UserAnswers] = {
    userAnswers
      .remove(ChildsNamePage(index))
      .flatMap(_.remove(ChildsBirthDatePage(index)))
      .flatMap(_.remove(QualifiesForDlaPage(index)))
      .flatMap(_.remove(DlaRatePage(index)))
  }
}
