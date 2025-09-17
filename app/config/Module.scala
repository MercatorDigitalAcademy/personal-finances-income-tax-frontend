package config

import com.google.inject.{AbstractModule, Provides}
import controllers.actions._

import java.time.Clock

class Module extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[DataRetrievalAction]).to(classOf[DataRetrievalActionImpl]).asEagerSingleton()
    bind(classOf[DataRequiredAction]).to(classOf[DataRequiredActionImpl]).asEagerSingleton()
    bind(classOf[Clock]).toInstance(java.time.Clock.systemDefaultZone)

    // We can’t use Configuration inside configure() here.
    // So move @Provides method outside configure() and take Configuration there
  }

  @Provides
  def provideIdentifierAction(
                               parser: play.api.mvc.BodyParsers.Default,
                               frontendAppConfig: config.FrontendAppConfig,
                               authConnector: uk.gov.hmrc.auth.core.AuthConnector
                             )(implicit ec: scala.concurrent.ExecutionContext, configuration: play.api.Configuration): IdentifierAction = {
    val fakeAuth = configuration.getOptional[Boolean]("features.fakeAuth").getOrElse(false)
    if (fakeAuth) new FakeIdentifierAction(parser)
    else new AuthenticatedIdentifierAction(authConnector, frontendAppConfig, parser)
  }
}
