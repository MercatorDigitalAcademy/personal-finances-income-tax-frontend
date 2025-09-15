package forms

import forms.behaviours.CheckboxFieldBehaviours
import models.DoYouHaveAnyChildren
import play.api.data.FormError

class DoYouHaveAnyChildrenFormProviderSpec extends CheckboxFieldBehaviours {

  val form = new DoYouHaveAnyChildrenFormProvider()()

  ".value" - {

    val fieldName = "value"
    val requiredKey = "doYouHaveAnyChildren.error.required"

    behave like checkboxField[DoYouHaveAnyChildren](
      form,
      fieldName,
      validValues  = DoYouHaveAnyChildren.values,
      invalidError = FormError(s"$fieldName[0]", "error.invalid")
    )

    behave like mandatoryCheckboxField(
      form,
      fieldName,
      requiredKey
    )
  }
}
