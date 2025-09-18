package forms

import javax.inject.Inject

import forms.mappings.Mappings
import play.api.data.Form
import models.IsEntitledToDla

class IsEntitledToDlaFormProvider @Inject() extends Mappings {

  def apply(): Form[IsEntitledToDla] =
    Form(
      "value" -> enumerable[IsEntitledToDla]("isEntitledToDla.error.required")
    )
}
