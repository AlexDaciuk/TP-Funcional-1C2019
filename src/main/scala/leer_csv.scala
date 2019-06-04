import java.io.File
import doobie._
import doobie.implicits._
import doobie.util.ExecutionContexts
import cats._
import cats.data._
import cats.effect._
import kantan.csv._ // All kantan.csv types.
import kantan.csv.ops._ // Enriches types with useful methods.
import kantan.codecs.resource.ResourceIterator
import doobie._
import doobie.implicits._
import doobie.util.ExecutionContexts
import fs2.Stream

object leerCSV extends App {

  // Abro el archivo y apunto un csvReader a ese archivo
  val data = new File("assets/csv/train.csv")
  val reader = data.asCsvReader[List[String]](rfc.withHeader)

  // Me conecto a la db
  implicit val cs = IO.contextShift(ExecutionContexts.synchronous)
  val conexion = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver", // driver classname
    "jdbc:postgresql:funcional", // connect URL (driver-specific)
    "funcional", // user
    "", // password
    ExecutionContexts.synchronous // just for testing
  )

  var crearTable = sql"""
            create table data(
            maiScore  integer,
            deviceMatch  integer,
            factorCodes  integer,
            firstEncounter  integer,
            icAddress  integer,
            icintegerernet  integer,
            icSuspicious  integer,
            icVelocity  integer,
            icIdentity  integer,
            ipRoutingMethod  integer,
            reasonCode  integer,
            timeOnPage  integer,
            billinCountryCode  text,
            cancelled  integer,
            cardContryCode  text,
            pp_1  integer,
            pp_30  integer,
            pp_60  integer,
            pp_90  integer,
            caseDate  text,
            caseMinuteDistance  integer,
            casesCount  integer,
            channel  text,
            correlID  integer,
            countDifferentCards  integer,
            countDifferentInstallments  integer,
            countryCode  text,
            countryFrom  text,
            countryTo  text,
            distanceToArrival  real,
            distanceToDeparture  real,
            domainProc  real,
            maiAdvice  integer,
            maiVerification  text,
            maiReason  integer,
            maiRisk  integer,
            maibisScore  integer,
            maiStatus  integer,
            maiUnique  integer,
            maiAvgSecs  integer,
            maiBuys  integer,
            maySearches  integer,
            eulerBadge  text,
            maiPax  text,
            maiType  text,
            maiRels  text,
            maiApp  integer,
            mai_urgency   text,
            mai_network   text,
            mai_all_pax   text,
            maiLastSeconds  integer,
            apocrypha  integer,
            friendly  integer,
            hours_since_last_verification  integer,
            iataFrom  text,
            iataTo  text,
            ip_city  text,
            mai_language  text,
            mai_negative  integer,
            mai_suspect  integer,
            mai_os  text,
            mai_policy_score  integer,
            mai_pulevel  integer,
            maibis_reason  text,
            mai_city  text,
            mai_region  text,
            maitris_score  integer,
            lag_time_hours  integer,
            many_holders_for_card  real,
            many_name_for_document  real,
            online_airport_state  integer,
            online_billing_address  integer,
            online_cep_number_bond  integer,
            online_city_bond  integer,
            online_ddd  integer,
            online_ddd_bond  integer,
            online_death  integer,
            online_email  integer,
            online_family_bond  integer,
            online_ip_state  integer,
            online_name  integer,
            online_code  integer,
            online_phone  integer,
            online_queries  integer,
            online_state_bond  integer,
            payment_card_type  text,
            payment_installments  integer,
            same_field_features  text,
            speed_to_departure  real,
            total_usd_amounts  integer,
            triangulation_height  real,
            triangulation_height_speed  real,
            trip_distance  real
          ) """.update.run

  case class PrimeraParte(maiScore: Int,
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
                          caseDate: String,
                          caseMinuteDistance: Int)

  case class SegundaParte(casesCount: Int,
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
                          maiReason: Int,
                          maiRisk: Int,
                          maibisScore: Int,
                          maiStatus: Int,
                          maiUnique: Int,
                          maiAvgSecs: Int,
                          maiBuys: Int,
                          maySearches: Int)

  case class TerceraParte(
      eulerBadge: String,
      maiPax: String,
      maiType: String,
      maiRels: String,
      maiApp: Int,
      mai_urgency: String,
      mai_network: String,
      mai_all_pax: String,
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
      maibis_reason: String
  )

  case class CuartaParte(mai_city: String,
                         mai_region: String,
                         maitris_score: Int,
                         lag_time_hours: Int,
                         many_holders_for_card: Float,
                         many_name_for_document: Float,
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
                         online_queries: Int,
                         online_state_bond: Int,
                         payment_card_type: String)

  case class QuintaParte(
      payment_installments: Int,
      same_field_features: String,
      speed_to_departure: Float,
      total_usd_amounts: Int,
      triangulation_height: Float,
      triangulation_height_speed: Float,
      trip_distance: Float
  )

  def procesar_primera_parte(list: List[String]): PrimeraParte = {
    val slice1 = lista.slice(0, 21)
    val primeraParte = PrimeraParte(
      slice1(0).toInt,
      slice1(1).toInt,
      slice1(2).toInt,
      slice1(3).toInt,
      slice1(4).toInt,
      slice1(5).toInt,
      slice1(6).toInt,
      slice1(7).toInt,
      slice1(8).toInt,
      slice1(9).toInt,
      slice1(10).toInt,
      slice1(11).toInt,
      slice1(12),
      slice1(13).toInt,
      slice1(14),
      slice1(15).toInt,
      slice1(16).toInt,
      slice1(17).toInt,
      slice1(18).toInt,
      slice1(19),
      slice1(20).toInt
    )
  }

  def procesar_segunda_parte(lista: List[String]): SegundaParte = {
    val slice2 = lista.slice(21, 42)
    val segundaParte = SegundaParte(
      slice2(0).toInt,
      slice2(1),
      slice2(2).toInt,
      slice2(3).toInt,
      slice2(4).toInt,
      slice2(5),
      slice2(6),
      slice2(7),
      slice2(8).toFloat,
      slice2(9).toFloat,
      slice2(10).toFloat,
      slice2(11).toInt,
      slice2(12),
      slice2(13).toInt,
      slice2(14).toInt,
      slice2(15).toInt,
      slice2(16).toInt,
      slice2(17).toInt,
      slice2(18).toInt,
      slice2(19).toInt,
      slice2(20).toInt
    )
  }

  def procesar_tercera_parte(lista: List[String]): TerceraParte = {
    val slice3 = lista.slice(42, 64)
    val terceraParte = TerceraParte(
      slice3(0),
      slice3(1),
      slice3(2),
      slice3(3),
      slice3(4).toInt,
      slice3(5).toInt,
      slice3(6).toInt,
      slice3(7).toInt,
      slice3(8).toInt,
      slice3(9),
      slice3(10),
      slice3(11),
      slice3(12),
      slice3(13).toInt,
      slice3(14).toInt,
      slice3(15),
      slice3(16).toInt,
      slice3(17).toInt,
      slice3(18),
      slice3(19).toInt,
      slice3(20).toInt,
      slice3(21)
    )
  }

  def procesar_cuarta_parte(lista: List[String]): CuartaParte = {
    val slice4 = lista.slice(64, 86)
    val cuarteParte = CuartaParte(
      slice4(0),
      slice4(1).toInt,
      slice4(2).toInt,
      slice4(3).toFloat,
      slice4(4).toFloat,
      slice4(5).toInt,
      slice4(6).toInt,
      slice4(7).toInt,
      slice4(8).toInt,
      slice4(9).toInt,
      slice4(10).toInt,
      slice4(11).toInt,
      slice4(12).toInt,
      slice4(13).toInt,
      slice4(14).toInt,
      slice4(15).toInt,
      slice4(16).toInt,
      slice4(17).toInt,
      slice4(18).toInt,
      slice4(19).toInt,
      slice4(20).toInt
    )
  }

  def procesar_quinta_parte(lista: Lista[String]): QuintaParte = {
    val slice5 = lista.slice(86, 92)
    val quintaParte = QuintaParte(
      slice5(0).toInt,
      slice5(1),
      slice5(2).toFloat,
      slice5(3).toInt,
      slice5(4).toFloat,
      slice5(5).toFloat,
      slice5(6).toFloat
    )
  }

  def cargar_db(lista: List[String]): Either[String, Unit] = {}

  def iterador(lista: ReadResult[List[String]]): Either[String, String] = {
    println("Entre")
    lista match {
      case Right(l) => {
        cargar_db(l) match {
          case Right(l) => iterador(reader.next())
          case Left(k) => Left("Fallo la carga a la db")
        }
      }
      case Left(k) => Left("Termino el archivo")
    }
  }

//  iterador(reader.next())

}
