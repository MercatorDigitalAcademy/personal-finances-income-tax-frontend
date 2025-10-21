package services

case class Deductions(
                       grossIncome: BigDecimal,
                       incomeTax: BigDecimal,
                       nationalInsurance: BigDecimal,
                       netIncome: BigDecimal
                     )


object TaxCalculator {

  /** Public entry */
  def calculate(income: BigDecimal): Deductions = {
    val incomeTax = calculateIncomeTax(income)
    val ni = calculateNI(income)
    val net = (income - incomeTax - ni).max(BigDecimal(0))
    Deductions(income, incomeTax, ni, net)
  }

  // ---------- Income Tax ----------
  // Personal allowance 12,570; tapers 1-for-2 above 100k; zero at 125,140.
  private val PersonalAllowance = BigDecimal(12570)
  private val AllowanceTaperStart = BigDecimal(100000)
  private val AllowanceZeroAt = BigDecimal(125140)

  // Tax bands applied to taxable income (not including allowance).
  private val BasicRateUpper = BigDecimal(50270) // 20%
  private val HigherRateUpper = BigDecimal(125140) // 40%
  private val BasicRate = BigDecimal("0.20")
  private val HigherRate = BigDecimal("0.40")
  private val AdditionalRate = BigDecimal("0.45")

  private def personalAllowanceFor(income: BigDecimal): BigDecimal =
    if (income <= AllowanceTaperStart) PersonalAllowance
    else if (income >= AllowanceZeroAt) BigDecimal(0)
    else (PersonalAllowance - ((income - AllowanceTaperStart) / 2)).max(0)

  private def calculateIncomeTax(income: BigDecimal): BigDecimal = {
    val allowance = personalAllowanceFor(income)
    val taxable = (income - allowance).max(0)

    val basicPortion = taxable.min(BasicRateUpper - PersonalAllowance).max(0)
    val higherPortion = (taxable - basicPortion).min(HigherRateUpper - BasicRateUpper).max(0)
    val addPortion = (taxable - basicPortion - higherPortion).max(0)

    val basicTax = basicPortion * BasicRate
    val higherTax = higherPortion * HigherRate
    val addTax = addPortion * AdditionalRate

    // Extra 40% on lost allowance between 100k-125,140
    val lostAllowance = (PersonalAllowance - allowance).max(0)
    val lostAllowanceTax = lostAllowance * HigherRate

    basicTax + higherTax + addTax + lostAllowanceTax
  }

  // ---------- National Insurance ----------
  // 8% between 12,576 and 50,268; 2% above 50,268
  private val NILower = BigDecimal(12576)
  private val NIMid = BigDecimal(50268)
  private val NIRateMain = BigDecimal("0.08")
  private val NIRateUpper = BigDecimal("0.02")

  private def calculateNI(income: BigDecimal): BigDecimal = {
    val mainPortion = income.min(NIMid).max(NILower) - NILower // 12,576–50,268
    val upperPortion = (income - NIMid).max(0)

    (mainPortion.max(0) * NIRateMain) + (upperPortion * NIRateUpper)
  }
}
