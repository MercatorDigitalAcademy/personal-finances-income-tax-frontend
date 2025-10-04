package forms.benefits

import forms.behaviours.BooleanFieldBehaviours
import play.api.data.FormError

class DeleteChildFormProviderSpec extends BooleanFieldBehaviours {

  val requiredKey = "deleteChild.error.required"
  val invalidKey = "error.boolean"

  val form = new DeleteChildFormProvider()()

  ".value" - {

    val fieldName = "value"

    behave like booleanField(
      form,
      fieldName,
      invalidError = FormError(fieldName, invalidKey)
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
