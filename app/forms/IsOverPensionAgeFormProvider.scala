package forms

import forms.mappings.Mappings
import play.api.data.Form

import javax.inject.Inject

class IsOverPensionAgeFormProvider @Inject() extends Mappings {

  def apply(): Form[Boolean] =
    Form(
      "value" -> boolean("isOverPensionAge.error.required")
    )
}
