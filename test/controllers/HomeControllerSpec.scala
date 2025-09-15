package controllers

import base.SpecBase
import play.api.Application
import play.api.test._
import play.api.test.Helpers._
import views.html.{ContinueView, IndexView}

class HomeControllerSpec extends SpecBase {

  val application: Application = applicationBuilder(userAnswers = None).build()
  lazy val indexView: IndexView = application.injector.instanceOf[IndexView]
  lazy val continueView: ContinueView = application.injector.instanceOf[ContinueView]

  "HomeController GET" - {

    "render the index page from a new instance of controller" in {
      val controller = new HomeController(stubMessagesControllerComponents(), indexView,continueView )
      val home = controller.onPageLoad().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include("index.heading")

    }

    "render the index page from the application" in {
      val controller = application.injector.instanceOf[HomeController]
      val home = controller.onPageLoad().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Personal Finance Frontend")
    }

    "render the index page from the router" in {
      val request = FakeRequest(GET, "/")
      val home = route(application, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Personal Finance Frontend")
    }
  }
}
