package forms

import forms.behaviours.CheckboxFieldBehaviours
import models.DoYouHaveAnyChildrenDoYouHaveAnyChildren
import play.api.data.FormError

class DoYouHaveAnyChildrenDoYouHaveAnyChildrenFormProviderSpec extends CheckboxFieldBehaviours {

  val form = new DoYouHaveAnyChildrenDoYouHaveAnyChildrenFormProvider()()

  ".value" - {

    val fieldName = "value"
    val requiredKey = "doYouHaveAnyChildrenDoYouHaveAnyChildren.error.required"

    behave like checkboxField[DoYouHaveAnyChildrenDoYouHaveAnyChildren](
      form,
      fieldName,
      validValues  = DoYouHaveAnyChildrenDoYouHaveAnyChildren.values,
      invalidError = FormError(s"$fieldName[0]", "error.invalid")
    )

    behave like mandatoryCheckboxField(
      form,
      fieldName,
      requiredKey
    )
  }
}
