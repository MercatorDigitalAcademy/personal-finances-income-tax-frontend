import sbt.*

object AppDependencies {


  val compile: Seq[ModuleID] = Seq(
    play.sbt.PlayImport.ws,
    "uk.gov.hmrc" %% "play-frontend-hmrc-play-30" % "12.7.0",
    "uk.gov.hmrc" %% "play-conditional-form-mapping-play-30" % "3.3.0",
    "uk.gov.hmrc" %% "bootstrap-frontend-play-30" % "9.13.0",
    "uk.gov.hmrc.mongo" %% "hmrc-mongo-play-30" % "2.7.0",
  )
  val test: Seq[ModuleID] = Seq(
    "org.scalamock" %% "scalamock" % "7.5.0" % Test,
    "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test,
    "org.scalatest" %% "scalatest" % "3.2.18" % Test,
    "org.scalatestplus" %% "scalacheck-1-17" % "3.2.18.0"
  ).map(_ % Test)


  def apply(): Seq[ModuleID] = compile ++ test
}
