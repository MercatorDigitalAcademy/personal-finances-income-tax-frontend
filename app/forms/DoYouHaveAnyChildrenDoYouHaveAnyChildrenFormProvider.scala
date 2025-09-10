package forms

import javax.inject.Inject

import forms.mappings.Mappings
import play.api.data.Form
import play.api.data.Forms.set
import models.DoYouHaveAnyChildrenDoYouHaveAnyChildren

class DoYouHaveAnyChildrenDoYouHaveAnyChildrenFormProvider @Inject() extends Mappings {

  def apply(): Form[Set[DoYouHaveAnyChildrenDoYouHaveAnyChildren]] =
    Form(
      "value" -> set(enumerable[DoYouHaveAnyChildrenDoYouHaveAnyChildren]("doYouHaveAnyChildrenDoYouHaveAnyChildren.error.required")).verifying(nonEmptySet("doYouHaveAnyChildrenDoYouHaveAnyChildren.error.required"))
    )
}
