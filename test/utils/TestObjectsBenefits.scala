package utils

import models.Child
import models.DlaRate.Higher
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryList
import viewmodels.govuk.all.SummaryListViewModel

import java.time.LocalDate

trait TestObjectsBenefits {

  val emptySummaryList: SummaryList = SummaryListViewModel(Seq.empty)
  val summaryList: SummaryList = SummaryListViewModel(Seq.empty)
  val childList: List[Child] = List(Child("Mary Jane Philips", LocalDate.of(2022,1,11), qualifiesForDla = true, Some(Higher)))
  val emptyList: List[Nothing] = List.empty

}
