package forms

import forms.mappings.Mappings
import javax.inject.Inject
import play.api.data.{Form, Forms}
import play.api.data.Forms.mapping

case class TaxFormData(annualIncome: BigDecimal, taxYear: String)

class TaxFormFormProvider @Inject() extends Mappings {

  private val maxIncome: Int = Int.MaxValue

  def apply(validTaxYears: Seq[String]): Form[TaxFormData] =
    Form(
      mapping(
        "annualIncome" -> currency(
          "taxForm.annualIncome.error.required",
          "taxForm.annualIncome.error.invalidNumeric",
          "taxForm.annualIncome.error.nonNumeric"
        ).verifying(maximumCurrency(maxIncome, "taxForm.annualIncome.error.aboveMaximum")),
        "taxYear" -> Forms.text
          .verifying("taxForm.taxYear.error.required", _.nonEmpty)
          .verifying("taxForm.taxYear.error.invalid", y => validTaxYears.contains(y))
      )(TaxFormData.apply)(TaxFormData.unapply)
    )
}
