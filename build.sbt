//name := """personal-finances-dashboard-frontend"""
//organization := "com.academy"
//
//version := "1.0-SNAPSHOT"
//
//lazy val root = (project in file(".")).enablePlugins(PlayScala)
//
//scalaVersion := "2.13.16"
//
//libraryDependencies += guice
//libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.2" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.academy.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.academy.binders._"
import play.sbt.routes.RoutesKeys
import sbt.Def


lazy val appName: String = """personal-finances-dashboard-frontend"""
organization := "com.academy"

ThisBuild / version := "1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.16"

resolvers += MavenRepository("HMRC-open-artefacts-maven2", "https://open.artefacts.tax.service.gov.uk/maven2")
libraryDependencies ++= AppDependencies()
libraryDependencies ++= Seq(
  guice
)

lazy val microservice = (project in file("."))
  .enablePlugins(PlayScala)
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .settings(inConfig(Test)(testSettings): _*)
  .settings(ThisBuild / useSuperShell := false)
  .settings(
    name := appName,
    RoutesKeys.routesImport ++= Seq(
      "models._",
      "uk.gov.hmrc.play.bootstrap.binders.RedirectUrl"
    ),
    TwirlKeys.templateImports ++= Seq(
      "play.twirl.api.HtmlFormat",
      "play.twirl.api.HtmlFormat._",
      "uk.gov.hmrc.govukfrontend.views.html.components._",
      "uk.gov.hmrc.hmrcfrontend.views.html.components._",
      "uk.gov.hmrc.hmrcfrontend.views.html.helpers._",
      "uk.gov.hmrc.hmrcfrontend.views.config._",
      "views.ViewUtils._",
      "models.Mode",
      "controllers.routes._",
      "viewmodels.govuk.all._"
    ),
    PlayKeys.playDefaultPort := 17000,
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-Wconf:src=html/.*:s",
      "-Wconf:src=routes/.*:s",
      "-Wconf:msg=Flag.*repeatedly:s"
    ),
    retrieveManaged := true
  )

lazy val testSettings: Seq[Def.Setting[_]] = Seq(
  fork := true,
  unmanagedSourceDirectories += baseDirectory.value / "test-utils"
)

lazy val it =
  (project in file("it"))
    .enablePlugins(PlayScala)
    .dependsOn(microservice % "test->test")

addCommandAlias("runAllChecks", ";clean;compile;scalafmtAll;coverage;test;it/test;scalastyle;coverageReport")

