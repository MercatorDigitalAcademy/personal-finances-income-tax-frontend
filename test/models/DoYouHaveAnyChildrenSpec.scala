package models

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.OptionValues
import play.api.libs.json.{JsError, JsString, Json}

class DoYouHaveAnyChildrenSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with OptionValues {

  "DoYouHaveAnyChildren" - {

    "must deserialise valid values" in {

      val gen = Gen.oneOf(DoYouHaveAnyChildren.values)

      forAll(gen) {
        doYouHaveAnyChildren =>

          JsString(doYouHaveAnyChildren.toString).validate[DoYouHaveAnyChildren].asOpt.value mustEqual doYouHaveAnyChildren
      }
    }

    "must fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!DoYouHaveAnyChildren.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[DoYouHaveAnyChildren] mustEqual JsError("error.invalid")
      }
    }

    "must serialise" in {

      val gen = Gen.oneOf(DoYouHaveAnyChildren.values)

      forAll(gen) {
        doYouHaveAnyChildren =>

          Json.toJson(doYouHaveAnyChildren) mustEqual JsString(doYouHaveAnyChildren.toString)
      }
    }
  }
}
