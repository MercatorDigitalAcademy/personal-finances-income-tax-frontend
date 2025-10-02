package forms.benefits

import forms.mappings.Mappings
import play.api.data.Form
import play.api.i18n.Messages

import java.time.LocalDate
import javax.inject.Inject

class ChildsBirthDateFormProvider @Inject() extends Mappings {

  def apply()(implicit messages: Messages): Form[LocalDate] =
    Form(
      "value" -> localDate(
        invalidKey = "childsBirthDate.error.invalid",
        allRequiredKey = "childsBirthDate.error.required.all",
        twoRequiredKey = "childsBirthDate.error.required.two",
        requiredKey = "childsBirthDate.error.required"
      )
    )
}
