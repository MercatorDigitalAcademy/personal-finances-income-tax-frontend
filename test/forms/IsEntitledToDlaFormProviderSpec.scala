package forms

import forms.behaviours.OptionFieldBehaviours
import models.IsEntitledToDla
import play.api.data.FormError

class IsEntitledToDlaFormProviderSpec extends OptionFieldBehaviours {

  val form = new IsEntitledToDlaFormProvider()()

  ".value" - {

    val fieldName = "value"
    val requiredKey = "isEntitledToDla.error.required"

    behave like optionsField[IsEntitledToDla](
      form,
      fieldName,
      validValues  = IsEntitledToDla.values,
      invalidError = FormError(fieldName, "error.invalid")
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
