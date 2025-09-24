package forms.benefits

import forms.mappings.Mappings
import play.api.data.Form

import javax.inject.Inject

class AddAChildFormProvider @Inject() extends Mappings {

  def apply(): Form[Boolean] =
    Form(
      "value" -> boolean("addAChild.error.required")
    )
}
