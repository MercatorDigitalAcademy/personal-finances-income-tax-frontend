package forms.benefits

import javax.inject.Inject

import forms.mappings.Mappings
import play.api.data.Form

class SubmitChildrenFormProvider @Inject() extends Mappings {

  def apply(): Form[Boolean] =
    Form(
      "value" -> boolean("submitChildren.error.required")
    )
}
