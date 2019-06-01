import java.io.File
import cats.effect._
import kantan.csv._ // All kantan.csv types.
import kantan.csv.ops._ // Enriches types with useful methods.
import kantan.codecs.resource.ResourceIterator
import doobie._
import doobie.implicits._
import doobie.util.ExecutionContexts
import cats._
import cats.data._
import cats.implicits._
import fs2.Stream

object leerCSV extends App {

  // Abro el archivo y apunto un csvReader a ese archivo
  val data = new File("assets/csv/train.csv")
  val reader = data.asCsvReader[List[String]](rfc.withHeader)

  // Me conecto a la db
  val conexion = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver", // driver classname
    "jdbc:postgresql:funcional", // connect URL (driver-specific)
    "funcional", // user
    "", // password
    ExecutionContexts.synchronous // just for testing
  )

  case class PrimeraParte(
      maiScore: Int,
      deviceMatch: Int,
      factorCodes: Int,
      firstEncounter: Int,
      icAddress: Int,
      icInternet: Int,
      icSuspicious: Int,
      icVelocity: Int,
      icIdentity: Int,
      ipRoutingMethod: Int,
      reasonCode: Int,
      timeOnPage: Int,
      billinCountryCode: String,
      cancelled: Int,
      cardContryCode: String,
      pp_1: Int,
      pp_30: Int,
      pp_60: Int,
      pp_90: Int,
      caseDate: String, // Es una fecha, lo mando como string
      caseMinuteDistance: Int)

  case class SegundaParte(
      casesCount: Int,
      channel: String,
      correlID: Int,
      countDifferentCards: Int,
      countDifferentInstallments: Int,
      countryCode: String,
      countryFrom: String,
      countryTo: String,
      distanceToArrival: Float,
      distanceToDeparture: Float,
      domainProc: Float,
      maiAdvice: Int,
      maiVerification: String,
      maiReason: Int, // maiVerification es otra fecha, idem caseDate
      maiRisk: Int,
      maibisScore: Int,
      maiStatus: Int,
      maiUnique: Int,
      maiAvgSecs: Int,
      maiBuys: Int,
      maySearches: Int)

  case class TerceraParte(
      eulerBadge: String, // eulerBadge es una especie de enum creo, ya que solamente veo 3 strings como valores
      maiPax: String,
      maiType: String,
      maiRels: String,
      maiApp: Int, // maiPax es un bar separated values ¯\_(ツ)_/¯
      maiLastSeconds: Int,
      apocrypha: Int,
      friendly: Int,
      hours_since_last_verification: Int,
      iataFrom: String,
      iataTo: String,
      ip_city: String,
      mai_language: String,
      mai_negative: Int,
      mai_suspect: Int,
      mai_os: String,
      mai_policy_score: Int,
      mai_pulevel: Int,
      maibis_reason: String,
      mai_city: String
  )

  case class CuartaParte(mai_region: String,
                         maitris_score: Int,
                         lag_time_hours: Int,
                         many_holders_for_card: Double,
                         many_name_for_document: Double,
                         online_airport_state: Int,
                         online_billing_address: Int,
                         online_cep_number_bond: Int,
                         online_city_bond: Int,
                         online_ddd: Int,
                         online_ddd_bond: Int,
                         online_death: Int,
                         online_email: Int,
                         online_family_bond: Int,
                         online_ip_state: Int,
                         online_name: Int,
                         online_code: Int,
                         online_phone: Int,
                         online_query: Int,
                         online_state_bond: Int,
                         payment_card_type: String)

  case class QuintaParte(
      payment_installments: Int,
      same_field_features: String,
      speed_to_departure: Double,
      total_usd_amounts: Int,
      triangulation_height: Double,
      triangulation_height_speed: Double,
      trip_distance: Double
  )

//  def cargar_db(lista: List[String],
//               // session: Resource[IO, SessionIO]
//             ):
//             //Either[String, String]
//             Unit = {}
//
//  def iterador(lista: ReadResult[List[String]]): Either[String, String] = {
//    println("Entre")
//    lista match {
//      case Right(l) => {
//        cargar_db(l) match {
//          case Right(l) => iterador(reader.next())
//          case Left(k) => Left("Fallo la carga a la db")
//        }
//      }
//      case Left(k) => Left("Termino el archivo")
//    }
//  }

//  iterador(reader.next())

}
