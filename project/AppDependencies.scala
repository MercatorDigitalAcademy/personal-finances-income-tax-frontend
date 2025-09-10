import sbt.*

object AppDependencies {

  val compile: Seq[ModuleID] = Seq(
    play.sbt.PlayImport.ws,
    "uk.gov.hmrc"          %% "play-frontend-hmrc-play-30"            % "12.1.0",
    "uk.gov.hmrc"          %% "play-conditional-form-mapping-play-30" % "3.3.0",
    "uk.gov.hmrc"          %% "bootstrap-frontend-play-30"            % bootstrapVersion,
    "uk.gov.hmrc.mongo"    %% "hmrc-mongo-play-30"                    % hmrcMongoVersion,
//    "com.github.tototoshi" %% "scala-csv"                             % "2.0.0"
  )
  val test: Seq[ModuleID] = Seq(
    "org.scalatest"          %% "scalatest"            % "3.2.15" % Test,
    "org.scalamock"          %% "scalamock"            % "5.2.0"  % Test
  ).map(_ % Test)
  private val bootstrapVersion = "9.13.0"
  private val hmrcMongoVersion = "2.7.0"

  def apply(): Seq[ModuleID] = compile ++ test
}
