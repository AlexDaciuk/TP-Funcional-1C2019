scalaVersion := "2.12.7" // Scala 2.12/11

scalacOptions += "-Ypartial-unification" // 2.11.9+

lazy val doobieVersion = "0.7.0"

libraryDependencies ++= Seq(
  "org.tpolecat" %% "doobie-core"     % doobieVersion,
  "org.tpolecat" %% "doobie-postgres" % doobieVersion,
  "org.tpolecat" %% "doobie-specs2"   % doobieVersion
)

libraryDependencies += "org.typelevel" %% "cats-core" % "1.6.0"
libraryDependencies += "org.typelevel" %% "cats-effect" % "1.3.0"
libraryDependencies += "com.nrinaudo" %% "kantan.csv" % "0.5.0"
libraryDependencies += "com.nrinaudo" %% "kantan.csv-cats" % "0.5.0"
