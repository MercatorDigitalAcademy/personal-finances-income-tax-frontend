package forms

import forms.mappings.Mappings
import models.DlaRate
import play.api.data.Form

import javax.inject.Inject

class DlaRateFormProvider @Inject() extends Mappings {

  def apply(): Form[DlaRate] =
    Form(
      "value" -> enumerable[DlaRate]("dlaRate.error.required")
    )
}
