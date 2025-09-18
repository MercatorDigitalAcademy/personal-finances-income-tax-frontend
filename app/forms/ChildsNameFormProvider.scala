package forms

import javax.inject.Inject

import forms.mappings.Mappings
import play.api.data.Form

class ChildsNameFormProvider @Inject() extends Mappings {

  def apply(): Form[String] =
    Form(
      "value" -> text("childsName.error.required")
        .verifying(maxLength(70, "childsName.error.length"))
    )
}
