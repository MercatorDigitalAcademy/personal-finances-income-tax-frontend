package forms.benefits

import forms.behaviours.BooleanFieldBehaviours
import play.api.data.FormError

class SubmitChildrenFormProviderSpec extends BooleanFieldBehaviours {

  val requiredKey = "submitChildren.error.required"
  val invalidKey = "error.boolean"

  val form = new SubmitChildrenFormProvider()()

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
