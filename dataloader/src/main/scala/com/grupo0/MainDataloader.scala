package com.grupo0

import java.io.File

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{SQLContext, SparkSession}
import frameless.functions.aggregate._
import frameless.syntax._
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.{DoubleType, IntegerType, StringType, StructField, StructType, TimestampType}
import org.apache.spark.ml.feature.{StringIndexer, VectorAssembler}
import org.apache.spark.ml.{Pipeline, PipelineModel}
import org.jpmml.sparkml.PMMLBuilder
import org.apache.spark.ml.classification.RandomForestClassifier
import org.apache.spark.sql.DataFrame
// import spark.implicits._

object MainDataloader extends App  {
  val conf: SparkConf = new SparkConf().setMaster("local[*]").setAppName("frameless-first-example").set("spark.ui.enabled", "false")
  implicit val spark: SparkSession = SparkSession.builder().config(conf).appName("dataloader").getOrCreate()

  val schema: StructType = new StructType(Array(
    StructField("mai_score", IntegerType, false),
    StructField("DeviceMatch", IntegerType, false),
    StructField("FactorCodes", IntegerType, false),
    StructField("FirstEncounter", IntegerType, false),
    StructField("IcAddress", IntegerType, false),
    StructField("IcInternet", IntegerType, false),
    StructField("IcSuspicious", IntegerType, false),
    StructField("IcVelocity", IntegerType, false),
    StructField("Icidentity", IntegerType, false),
    StructField("IpRoutingMethod", IntegerType, false),
    StructField("ReasonCode", IntegerType, false),
    StructField("TimeOnPage", IntegerType, true),
    StructField("billingCountryCode", StringType, true),
    StructField("cancelled", IntegerType, false),
    StructField("cardCountryCode", StringType, false),
    StructField("pp_1", IntegerType, false),
    StructField("pp_30", IntegerType, false),
    StructField("pp_60", IntegerType, false),
    StructField("pp_90", IntegerType, false),
    StructField("caseDate", StringType, false),
    StructField("case_minutes_distance", IntegerType, true),
    StructField("cases_count", IntegerType, false),
    StructField("channel", StringType, false),
    StructField("correl_id", IntegerType, false),
    StructField("count_different_cards", IntegerType, true),
    StructField("count_different_installments", IntegerType, true),
    StructField("countryCode", StringType, false),
    StructField("countryFrom", StringType, false),
    StructField("countryTo", StringType, false),
    StructField("distance_to_arrival", DoubleType, false),
    StructField("distance_to_departure", DoubleType, false),
    StructField("domain_proc", StringType, true),
    StructField("mai_advice", IntegerType, false),
    StructField("mai_verification", StringType, true),
    StructField("mai_reason", IntegerType, true),
    StructField("mai_risk", IntegerType, true),
    StructField("maibis_score", IntegerType, true),
    StructField("mai_status", IntegerType, true),
    StructField("mai_unique", IntegerType, true),
    StructField("mai_avg_secs", IntegerType, true),
    StructField("mai_buys", IntegerType, true),
    StructField("mai_searches", IntegerType, true),
    StructField("eulerBadge", StringType, true),
    StructField("mai_pax", StringType, true),
    StructField("mai_type", StringType, true),
    StructField("mai_rels", StringType, true),
    StructField("mai_app", IntegerType, true),
    StructField("mai_urgency", StringType, true),
    StructField("mai_network", StringType, true),
    StructField("mai_all_pax", IntegerType, true),
    StructField("mai_last_secs", IntegerType, true),
    StructField("APOCRYPHA", IntegerType, true),
    StructField("friendly", IntegerType, false),
    StructField("hours_since_first_verification", StringType, true),
    StructField("iataFrom", StringType, false),
    StructField("iataTo", StringType, false),
    StructField("ip_city", StringType, false),
    StructField("mai_language", StringType, true),
    StructField("mai_negative", IntegerType, false),
    StructField("mai_suspect", IntegerType, false),
    StructField("mai_os", StringType, false),
    StructField("mai_policy_score", IntegerType, false),
    StructField("mai_pulevel", IntegerType, false),
    StructField("maibis_reason", StringType, false),
    StructField("mai_city", StringType, true),
    StructField("mai_region", StringType, false),
    StructField("maitris_score", IntegerType, false),
    StructField("lagTimeHours", IntegerType, true),
    StructField("many_holders_for_card", DoubleType, true),
    StructField("many_names_for_document", DoubleType, true),
    StructField("online_airport_state", IntegerType, false),
    StructField("online_billing_address_state", IntegerType, false),
    StructField("online_cep_number_bond", IntegerType, false),
    StructField("online_city_bond", IntegerType, false),
    StructField("online_ddd", IntegerType, false),
    StructField("online_ddd_bond", IntegerType, false),
    StructField("online_death", IntegerType, false),
    StructField("online_email", IntegerType, false),
    StructField("online_family_bond", IntegerType, false),
    StructField("online_ip_state", IntegerType, false),
    StructField("online_name", IntegerType, false),
    StructField("online_phone", IntegerType, false),
    StructField("online_queries", IntegerType, false),
    StructField("online_state_bond", IntegerType, false),
    StructField("paymentsCardType", StringType, false),
    StructField("paymentsInstallments", IntegerType, false),
    StructField("same_field_features", StringType, true),
    StructField("speed_to_departure", StringType, false),
    StructField("totalUsdAmount", StringType, false),
    StructField("triangulation_height", StringType, false),
    StructField("triangulation_height_speed", StringType, true),
    StructField("trip_distance", StringType, true)))

  // Table to query
  val tableName: String = "data"

  // Tell spark which driver to use
  val driver: String = "org.postgresql.Driver"
  val dbHost: String = scala.util.Properties.envOrElse("DB_HOST", "localhost")
 
  // Load the class
  Class.forName(driver)

  val df = spark
    .sqlContext.read
    .format("jdbc")
    .option("inferSchema", "true")
    .option("driver", driver)
    .option("url", s"jdbc:postgresql://${dbHost}:5432/funcional")
    .option("user", "funcional")
    .option("password", "")
    .option("dbtable", tableName)
    .load()

//  val df: DataFrame = spark.read.schema(schema).csv("assets/csv/train.csv")
  val columns: Array[(String, String)] = df.schema.fields.map(x=>(x.name, x.dataType.toString))
  val stringColumns: Array[String] = df.schema.fields.filter(x => x.dataType.toString == "StringType").map(x => x.name)

  val encodedColumns: Array[StringIndexer] = stringColumns.flatMap { name =>
    val indexer: StringIndexer = new StringIndexer()
      .setInputCol(name)
      .setOutputCol(name+"_index")
      .setHandleInvalid("skip")
    Array(indexer)
  }

  val pipeline: Pipeline = new Pipeline().setStages(encodedColumns)
  val indexerModel: PipelineModel = pipeline.fit(df)
  val encoded_df: DataFrame = indexerModel.transform(df)
    .drop(stringColumns: _*)
    .drop("caseDate")
    .drop("mai_verification") // caseDate y mai_verification son timestamps
    .drop("distance_to_arrival") // float
    .drop("distance_to_departure") // float
    .drop("domainproc") // float
    .drop("many_holders_for_card") // float
    .drop("many_name_for_document") // float
    .drop("speed_to_departure") // float
    .drop("triangulation_height") // float
    .drop("triangulation_height_speed") // float
    .drop("trip_distance") // float
    .drop("billincountrycode_index") // categories
    .drop("cardcontrycode_index") // categories
    .drop("casedate_index") // date
    .drop("countrycode_index") // categories
    .drop("countryfrom_index") // categories
    .drop("countryto_index") // categories
    .drop("mai_verification_index") // date
    .drop("maipax_index") // categories
    .drop("mai_rels_index") // categories
    .drop("mai_all_pax_index") // categories
    .drop("iatafrom_index") // categories
    .drop("iatato_index") // categories
    .drop("ip_city_index") // categories
    .drop("mai_language_index") // categories
    .drop("maibis_reason_index") // categories
    .drop("mai_city_index") // categories
    .drop("mai_region_index") // categories
    .drop("same_field_features_index") // categories

  val Array(training, test) = encoded_df.randomSplit(Array(0.8, 0.2), 5043)
  val inputCols: Array[String] = encoded_df.columns.diff(Seq("apocrypha"))
  val vectorAssembler: VectorAssembler = new VectorAssembler()
    .setInputCols(inputCols)
    .setOutputCol("features")

  // Hasta aca es el ETL

  val classifier: RandomForestClassifier = new RandomForestClassifier()
    .setImpurity("gini")
    .setMaxDepth(3)
    .setNumTrees(20)
    .setFeatureSubsetStrategy("auto")
    .setSeed(5043)
    .setLabelCol("apocrypha")

  val pip: Pipeline = new Pipeline().setStages(Array(vectorAssembler, classifier))
  val pipelineModel: PipelineModel = pip.fit(training)
  val FILE_NAME: String = "output/model.pmml"
  val pmml = new PMMLBuilder(training.schema, pipelineModel).buildFile(new File(FILE_NAME))

  // Closing
  spark.stop()
}
