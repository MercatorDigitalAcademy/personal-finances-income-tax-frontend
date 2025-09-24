package forms.benefits

import forms.mappings.Mappings
import play.api.data.Form

import javax.inject.Inject

class ChildsNameFormProvider @Inject() extends Mappings {

  def apply(): Form[String] =
    Form(
      "value" -> text("childsName.error.required")
        .verifying(maxLength(70, "childsName.error.length"))
    )
}
