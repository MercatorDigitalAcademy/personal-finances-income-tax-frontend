package controllers

import base.SpecBase
import play.api.test._
import play.api.test.Helpers._
import views.html.{ContinueView, IndexView}

import scala.Console.in

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class HomeControllerSpec extends SpecBase {

  val application = applicationBuilder(userAnswers = None).build()
  lazy val indexView = application.injector.instanceOf[IndexView]
  lazy val continueView = application.injector.instanceOf[ContinueView]

  "HomeController GET" - {

    "render the index page from a new instance of controller" - {
      val controller = new HomeController(stubMessagesControllerComponents(), indexView,continueView )
      val home = controller.onPageLoad().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play")
    }

    "render the index page from the application" - {
      val controller = application.injector.instanceOf[HomeController]
      val home = controller.onPageLoad().apply(FakeRequest(GET, "/"))

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play")
    }

    "render the index page from the router" - {
      val request = FakeRequest(GET, "/")
      val home = route(application, request).get

      status(home) mustBe OK
      contentType(home) mustBe Some("text/html")
      contentAsString(home) must include ("Welcome to Play")
    }
  }
}
