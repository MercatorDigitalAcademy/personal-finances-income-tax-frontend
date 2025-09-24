package forms.benefits

import forms.behaviours.BooleanFieldBehaviours
import play.api.data.FormError

class QualifiesForDlaFormProviderSpec extends BooleanFieldBehaviours {

  val requiredKey = "qualifiesForDla.error.required"
  val invalidKey = "error.boolean"

  val form = new QualifiesForDlaFormProvider()()

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
