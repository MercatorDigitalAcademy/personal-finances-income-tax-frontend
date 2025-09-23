package forms

import forms.behaviours.StringFieldBehaviours
import generators.Generators
import play.api.data.FormError

class ChildsNameFormProviderSpec extends StringFieldBehaviours with Generators {

  val requiredKey = "childsName.error.required"
  val lengthKey = "childsName.error.length"
  val maxLength = 70

  val form = new ChildsNameFormProvider()()

  ".value" - {

    val fieldName = "value"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      stringsWithMaxLength(maxLength)
    )

    behave like fieldWithMaxLength(
      form,
      fieldName,
      maxLength = maxLength,
      lengthError = FormError(fieldName, lengthKey, Seq(maxLength))
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
