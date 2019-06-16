lazy val Http4sVersion = "0.20.3"
lazy val CirceVersion = "0.11.1"
lazy val Specs2Version = "4.1.0"
lazy val LogbackVersion = "1.2.3"
lazy val doobieVersion = "0.7.0"

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

ProjectRef(file("../db-loader"), "db-loader")

lazy val root = (project in file("."))
  .settings(
    organization := "edu.spfsfiuba",
    name := "rest-service",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.8",
    scalacOptions ++= Seq("-Ypartial-unification"),
    libraryDependencies ++= Seq(
      "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"      %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "io.circe"        %% "circe-generic"       % CirceVersion,
      "org.specs2"      %% "specs2-core"         % Specs2Version % "test",
      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion,
      "org.tpolecat" %% "doobie-core"     % doobieVersion,
      "org.tpolecat" %% "doobie-postgres" % doobieVersion,
      "org.tpolecat" %% "doobie-specs2"   % doobieVersion,
      "org.typelevel" %% "cats-core" % "1.6.0",
      "org.typelevel" %% "cats-effect" % "1.3.0",
    ),
    addCompilerPlugin("org.spire-math" %% "kind-projector"     % "0.9.6"),
    addCompilerPlugin("com.olegpy"     %% "better-monadic-for" % "0.2.4")
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Ypartial-unification",
  "-Xfatal-warnings",
)

mainClass in Compile := Some("edu.spfsfiuba.restservice.Main")
dockerBaseImage := "openjdk:8"
