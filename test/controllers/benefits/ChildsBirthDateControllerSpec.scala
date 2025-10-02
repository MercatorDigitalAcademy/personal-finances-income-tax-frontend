package controllers.benefits

import base.SpecBase
import controllers.routes
import forms.benefits.ChildsBirthDateFormProvider
import models.{NormalMode, UserAnswers}
import navigation.{FakeNavigator, Navigator}
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import pages.benefits.ChildsBirthDatePage
import play.api.i18n.Messages
import play.api.inject.bind
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.{AnyContentAsEmpty, AnyContentAsFormUrlEncoded, Call}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.SessionRepository
import utils.TestObjectsBenefits
import views.html.ChildsBirthDateView

import java.time.{LocalDate, ZoneOffset}
import scala.concurrent.Future

class ChildsBirthDateControllerSpec extends SpecBase with MockitoSugar with TestObjectsBenefits {

  private implicit val messages: Messages = stubMessages()

  private val formProvider = new ChildsBirthDateFormProvider()
  private def form = formProvider()

  def onwardRoute = Call("GET", "/qualifiesForDla?index=0")

  val validAnswer = LocalDate.now(ZoneOffset.UTC)

  lazy val childsBirthDateRoute = controllers.benefits.routes.ChildsBirthDateController.onPageLoad(NormalMode, 0).url

  override val emptyUserAnswers = UserAnswers(userAnswersId)

  val json: JsObject = Json.obj(
    "data" -> Json.obj(
      "isUserClaimingChb" -> true,
      "children" -> Json.arr(
        Json.obj(
          "name" -> "First Child",
          "dateOfBirth" -> "2020-12-12",
          "qualifiesForDla" -> true,
          "dlaRate" -> "higher"
        )
      )
    )
  )

  val userAnswers = UserAnswers(userAnswersId, json)

  def getRequest(): FakeRequest[AnyContentAsEmpty.type] =
    FakeRequest(GET, childsBirthDateRoute)

  def postRequest(): FakeRequest[AnyContentAsFormUrlEncoded] =
    FakeRequest(POST, childsBirthDateRoute)
      .withFormUrlEncodedBody(
        "value.day"   -> validAnswer.getDayOfMonth.toString,
        "value.month" -> validAnswer.getMonthValue.toString,
        "value.year"  -> validAnswer.getYear.toString
      )

  val childsName: String = childList.head.childsName
  val thisChild: String = "this child."

  "ChildsBirthDate Controller" - {

    "must return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()
      val message: String = s"What is $childsName's date of birth'"

      running(application) {
        val result = route(application, getRequest()).value

        val view = application.injector.instanceOf[ChildsBirthDateView]

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, NormalMode, thisChild, 0)(getRequest(), messages(application)).toString
      }
    }

    "must populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = UserAnswers(userAnswersId).set(ChildsBirthDatePage(0), validAnswer).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      running(application) {
        val view = application.injector.instanceOf[ChildsBirthDateView]

        val result = route(application, getRequest()).value

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form.fill(validAnswer), NormalMode, thisChild, 0)(getRequest(), messages(application)).toString
      }
    }

    "must redirect to the next page when valid data is submitted" in {

      val mockSessionRepository = mock[SessionRepository]

      when(mockSessionRepository.set(any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(emptyUserAnswers))
          .overrides(
            bind[Navigator].toInstance(new FakeNavigator(onwardRoute)),
            bind[SessionRepository].toInstance(mockSessionRepository)
          )
          .build()

      running(application) {
        val result = route(application, postRequest()).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual onwardRoute.url
      }
    }

    "must return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      val request =
        FakeRequest(POST, childsBirthDateRoute)
          .withFormUrlEncodedBody(("value", "invalid value"))

      running(application) {
        val boundForm = form.bind(Map("value" -> "invalid value"))

        val view = application.injector.instanceOf[ChildsBirthDateView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST
        contentAsString(result) mustEqual view(boundForm, NormalMode, thisChild, 0)(request, messages(application)).toString
      }
    }

    "must redirect to Journey Recovery for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val result = route(application, getRequest()).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }

    "must redirect to Journey Recovery for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      running(application) {
        val result = route(application, postRequest()).value

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual routes.JourneyRecoveryController.onPageLoad().url
      }
    }
  }
}
