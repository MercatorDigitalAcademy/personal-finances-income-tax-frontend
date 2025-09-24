package pages.benefits

import models.UserAnswers
import pages._
import play.api.libs.json.JsPath

import scala.util.Try

case object AddAChildPage extends QuestionPage[Boolean] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "addAChild"

  def cleanup(userAnswers: UserAnswers): Try[UserAnswers] = {
    userAnswers
      .remove(ChildsNamePage)
      .flatMap(_.remove(ChildsBirthDatePage))
      .flatMap(_.remove(QualifiesForDlaPage))
      .flatMap(_.remove(DlaRatePage))
  }
}
