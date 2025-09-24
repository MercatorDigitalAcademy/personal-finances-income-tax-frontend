package forms.benefits

import forms.mappings.Mappings
import play.api.data.Form

import javax.inject.Inject

class QualifiesForDlaFormProvider @Inject() extends Mappings {

  def apply(): Form[Boolean] =
    Form(
      "value" -> boolean("qualifiesForDla.error.required")
    )
}
