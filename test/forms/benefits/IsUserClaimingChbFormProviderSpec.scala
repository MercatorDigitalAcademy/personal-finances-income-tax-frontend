package forms.benefits

import forms.behaviours.BooleanFieldBehaviours
import play.api.data.FormError

class IsUserClaimingChbFormProviderSpec extends BooleanFieldBehaviours {

  val requiredKey = "isUserClaimingChb.error.required"
  val invalidKey = "error.boolean"

  val form = new IsUserClaimingChbFormProvider()()

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
