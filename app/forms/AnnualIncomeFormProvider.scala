package forms

import forms.mappings.Mappings
import javax.inject.Inject
import play.api.data.Form

class AnnualIncomeFormProvider @Inject() extends Mappings {

  def apply(): Form[BigDecimal] =
    Form(
      "value" -> currency(
        "annualIncome.error.required",
        "annualIncome.error.invalidNumeric",
        "annualIncome.error.nonNumeric"
      )
        .verifying(maximumCurrency(Int.MaxValue, "annualIncome.error.aboveMaximum"))
    )
}
