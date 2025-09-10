package models

import generators.ModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.OptionValues
import play.api.libs.json.{JsError, JsString, Json}

class DoYouHaveAnyChildrenDoYouHaveAnyChildrenSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with OptionValues with ModelGenerators {

  "DoYouHaveAnyChildrenDoYouHaveAnyChildren" - {

    "must deserialise valid values" in {

      val gen = arbitrary[DoYouHaveAnyChildrenDoYouHaveAnyChildren]

      forAll(gen) {
        doYouHaveAnyChildrenDoYouHaveAnyChildren =>

          JsString(doYouHaveAnyChildrenDoYouHaveAnyChildren.toString).validate[DoYouHaveAnyChildrenDoYouHaveAnyChildren].asOpt.value mustEqual doYouHaveAnyChildrenDoYouHaveAnyChildren
      }
    }

    "must fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!DoYouHaveAnyChildrenDoYouHaveAnyChildren.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[DoYouHaveAnyChildrenDoYouHaveAnyChildren] mustEqual JsError("error.invalid")
      }
    }

    "must serialise" in {

      val gen = arbitrary[DoYouHaveAnyChildrenDoYouHaveAnyChildren]

      forAll(gen) {
        doYouHaveAnyChildrenDoYouHaveAnyChildren =>

          Json.toJson(doYouHaveAnyChildrenDoYouHaveAnyChildren) mustEqual JsString(doYouHaveAnyChildrenDoYouHaveAnyChildren.toString)
      }
    }
  }
}
