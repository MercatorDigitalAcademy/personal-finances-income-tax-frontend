package models

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.OptionValues
import play.api.libs.json.{JsError, JsString, Json}

class IsEntitledToDlaSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with OptionValues {

  "IsEntitledToDla" - {

    "must deserialise valid values" in {

      val gen = Gen.oneOf(IsEntitledToDla.values.toSeq)

      forAll(gen) {
        isEntitledToDla =>

          JsString(isEntitledToDla.toString).validate[IsEntitledToDla].asOpt.value mustEqual isEntitledToDla
      }
    }

    "must fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!IsEntitledToDla.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[IsEntitledToDla] mustEqual JsError("error.invalid")
      }
    }

    "must serialise" in {

      val gen = Gen.oneOf(IsEntitledToDla.values.toSeq)

      forAll(gen) {
        isEntitledToDla =>

          Json.toJson(isEntitledToDla) mustEqual JsString(isEntitledToDla.toString)
      }
    }
  }
}
