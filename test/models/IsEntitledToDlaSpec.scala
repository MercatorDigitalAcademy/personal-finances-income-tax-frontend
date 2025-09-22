package models

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.OptionValues
import play.api.libs.json.{JsError, JsString, Json}

class DlaRateSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with OptionValues {

  "DlaRate" - {

    "must deserialise valid values" in {

      val gen = Gen.oneOf(DlaRate.values.toSeq)

      forAll(gen) {
        dlaRate =>

          JsString(dlaRate.toString).validate[DlaRate].asOpt.value mustEqual dlaRate
      }
    }

    "must fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!DlaRate.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[DlaRate] mustEqual JsError("error.invalid")
      }
    }

    "must serialise" in {

      val gen = Gen.oneOf(DlaRate.values.toSeq)

      forAll(gen) {
        dlaRate =>

          Json.toJson(dlaRate) mustEqual JsString(dlaRate.toString)
      }
    }
  }
}
