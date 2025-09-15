package forms

import javax.inject.Inject
import forms.mappings.Mappings
import play.api.data.Form
import play.api.data.Forms.set
import models.DoYouHaveAnyChildren

class DoYouHaveAnyChildrenFormProvider @Inject() extends Mappings {

  def apply(): Form[Set[DoYouHaveAnyChildren]] =
    Form(
      "value" -> set(enumerable[DoYouHaveAnyChildren]("doYouHaveAnyChildren.error.required")).verifying(nonEmptySet("" +
        "doYouHaveAnyChildren.error.required"))
    )
}
