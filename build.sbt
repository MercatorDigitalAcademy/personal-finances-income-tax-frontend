import play.sbt.routes.RoutesKeys
import sbt.Def

lazy val appName: String = """personal-finances-income-tax-frontend"""
organization := "com.academy"

ThisBuild / version := "1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.16"

resolvers += MavenRepository(
  "HMRC-open-artefacts-maven2",
  "https://open.artefacts.tax.service.gov.uk/maven2"
)
val dependencies = Seq(
  "uk.gov.hmrc" %% "play-frontend-hmrc-play-30" % "12.7.0",
  "uk.gov.hmrc" %% "play-conditional-form-mapping-play-30" % "3.3.0",
  "uk.gov.hmrc" %% "bootstrap-frontend-play-30" % "9.13.0",
  "uk.gov.hmrc.mongo" %% "hmrc-mongo-play-30" % "2.7.0",
  guice,
  "org.scalamock" %% "scalamock" % "7.5.0" % Test,
  "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test,
  "org.scalatest" %% "scalatest" % "3.2.18" % Test,
  "org.scalatestplus" %% "scalacheck-1-17" % "3.2.18.0"
)

libraryDependencies ++= dependencies

lazy val microservice = (project in file("."))
  .enablePlugins(PlayScala)
  .disablePlugins(
    JUnitXmlReportPlugin
  ) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .settings(libraryDependencies ++= dependencies)
  .settings(inConfig(Test)(testSettings) *)
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
    PlayKeys.playDefaultPort := 9003,
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-Wconf:src=html/.*:s",
      "-Wconf:src=routes/.*:s",
      "-Wconf:msg=Flag.*repeatedly:s"
    ),
    retrieveManaged := true
  )

lazy val testSettings: Seq[Def.Setting[?]] = Seq(
  fork := true,
  unmanagedSourceDirectories += baseDirectory.value / "test-utils"
)

lazy val it =
  (project in file("it"))
    .enablePlugins(PlayScala)
    .dependsOn(microservice % "test->test")

addCommandAlias(
  "runAllChecks",
  ";clean;compile;scalafmtAll;coverage;test;it/test;scalastyle;coverageReport"
)
