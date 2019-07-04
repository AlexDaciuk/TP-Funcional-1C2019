scalaVersion := "2.12.8" // Scala 2.12/11

scalacOptions += "-Ypartial-unification" // 2.11.9+

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

lazy val doobieVersion = "0.7.0"

lazy val root = project.in(file(".")).
  settings(
    name := "db-loader",
    organization := "edu.spfsfiuba",
    scalaVersion := "2.12.8",
    version := "0.1",
    libraryDependencies ++= Seq(
      "org.tpolecat" %% "doobie-core"     % doobieVersion,
      "org.tpolecat" %% "doobie-postgres" % doobieVersion,
      "org.tpolecat" %% "doobie-specs2"   % doobieVersion,
      "org.typelevel" %% "cats-core" % "1.6.0",
      "org.typelevel" %% "cats-effect" % "1.3.0",
      "com.nrinaudo" %% "kantan.csv" % "0.5.1",
      "com.nrinaudo" %% "kantan.csv-cats" % "0.5.1",
    ),
  )

  mainClass in Compile := Some("edu.spfsfiuba.dbloader.MainDBLoader")
  dockerBaseImage := "openjdk:8"
