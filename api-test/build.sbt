scalaVersion := "2.12.8" // Also supports 2.10.x and 2.12.x

scalacOptions += "-Ypartial-unification" // 2.11.9+

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

// Only necessary for SNAPSHOT releases
resolvers += Resolver.sonatypeRepo("snapshots")

val http4sVersion = "0.20.3"

lazy val root = project.in(file(".")).
  settings(
    name := "apitest",
    organization := "com.grupo0",
    scalaVersion := "2.12.8",
    version := "0.1",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-dsl" % http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % http4sVersion,
      "org.http4s" %% "http4s-blaze-client" % http4sVersion,
      "org.http4s" %% "http4s-circe" % http4sVersion,
      "org.typelevel" %% "cats-core" % "1.6.0",
      "org.typelevel" %% "cats-effect" % "1.3.0",
      "com.nrinaudo" %% "kantan.csv" % "0.5.1",
      "com.nrinaudo" %% "kantan.csv-cats" % "0.5.1",
      "io.circe"  %% "circe-generic"  % "0.11.1",
      "io.circe" %% "circe-literal" % "0.11.1",
      "io.circe" %% "circe-parser" % "0.11.1"
    )
)

mainClass in Compile := Some("edu.spfsfiuba.apitest.ApiTest")
dockerBaseImage := "openjdk:8"
