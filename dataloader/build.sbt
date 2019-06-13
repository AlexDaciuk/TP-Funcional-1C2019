import com.typesafe.sbt.packager.docker._
lazy val SparkVersion = "2.4.3"
lazy val FramelessVersion = "0.6.1"

def makeColorConsole() = {
  val ansi = System.getProperty("sbt.log.noformat", "false") != "true"
  if (ansi) System.setProperty("scala.color", "true")
}

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

lazy val root = project.in(file(".")).
  settings(
    name := "dataloader",
    organization := "com.grupo0",
    scalaVersion := "2.11.12",
    version := "0.1",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "frameless-dataset" % FramelessVersion,
      "org.apache.spark" %% "spark-core" % SparkVersion,
      "org.apache.spark" %% "spark-sql"  % SparkVersion,
      "org.apache.spark" %% "spark-mllib"  % SparkVersion,
      // https://mvnrepository.com/artifact/org.postgresql/postgresql
      "org.postgresql" % "postgresql" % "9.4-1200-jdbc41",
      "org.jpmml" % "jpmml-xgboost" % "1.3.8",
      "org.jpmml" % "jpmml-sparkml" % "1.5.3"
    ),
    initialize ~= { _ => makeColorConsole() },
    initialCommands in console :=
      """
        |import org.apache.spark.{SparkConf, SparkContext}
        |import org.apache.spark.sql.SparkSession
        |import frameless.functions.aggregate._
        |import frameless.syntax._
        |
        |val conf = new SparkConf().setMaster("local[*]").setAppName("frameless-repl").set("spark.ui.enabled", "false")
        |implicit val spark = SparkSession.builder().config(conf).appName("dataloader").getOrCreate()
        |
        |import spark.implicits._
        |
        |spark.sparkContext.setLogLevel("WARN")
        |
        |import frameless.TypedDataset
      """.stripMargin,
    cleanupCommands in console :=
      """
        |spark.stop()
      """.stripMargin
  )

  fork := true

  mainClass in Compile := Some("com.grupo0.MainDataloader")
  dockerBaseImage := "openjdk:8"
