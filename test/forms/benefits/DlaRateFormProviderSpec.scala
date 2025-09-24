package forms.benefits

import forms.behaviours.OptionFieldBehaviours
import models.DlaRate
import play.api.data.FormError

class DlaRateFormProviderSpec extends OptionFieldBehaviours {

  val form = new DlaRateFormProvider()()

  ".value" - {

    val fieldName = "value"
    val requiredKey = "dlaRate.error.required"

    behave like optionsField[DlaRate](
      form,
      fieldName,
      validValues  = DlaRate.values,
      invalidError = FormError(fieldName, "error.invalid")
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )
  }
}
