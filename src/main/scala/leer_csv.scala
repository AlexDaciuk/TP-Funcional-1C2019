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
            icInternet  integer,
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
            online_phone  integer,
            online_queries  integer,
            online_state_bond  integer,
            payment_card_type  text,
            payment_installments  integer,
            same_field_features  json,
            speed_to_departure  real,
            total_usd_amounts  integer,
            triangulation_height  real,
            triangulation_height_speed  real,
            trip_distance  real
          ) """.update

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
                          distanceToArrival: Double,
                          distanceToDeparture: Double,
                          domainProc: Double,
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
                         online_phone: Int,
                         online_queries: Int,
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

  def toIntPropio(s: String): Int = {
    try {
      s.toInt
    } catch {
      case e: Exception => -1
    }
  }

  def toFloatPropio(s: String): Double = {
    try {
      s.toFloat
    } catch {
      case e: Exception => -1.0
    }
  }


  def procesar_primera_parte(lista: List[String]): PrimeraParte = {
    val slice1 = lista.slice(0, 21)
    println(slice1)
    val primeraParte = PrimeraParte(
      toIntPropio(slice1(0)),
      toIntPropio(slice1(1)),
      toIntPropio(slice1(2)),
      toIntPropio(slice1(3)),
      toIntPropio(slice1(4)),
      toIntPropio(slice1(5)),
      toIntPropio(slice1(6)),
      toIntPropio(slice1(7)),
      toIntPropio(slice1(8)),
      toIntPropio(slice1(9)),
      toIntPropio(slice1(10)),
      toIntPropio(slice1(11)),
      slice1(12),
      toIntPropio(slice1(13)),
      slice1(14),
      toIntPropio(slice1(15)),
      toIntPropio(slice1(16)),
      toIntPropio(slice1(17)),
      toIntPropio(slice1(18)),
      slice1(19),
      toIntPropio(slice1(20))
    )
    primeraParte
  }

  def procesar_segunda_parte(lista: List[String]): SegundaParte = {
    val slice2 = lista.slice(21, 42)
    println(slice2)
    val segundaParte = SegundaParte(
      toIntPropio(slice2(0)),
      slice2(1),
      toIntPropio(slice2(2)),
      toIntPropio(slice2(3)),
      toIntPropio(slice2(4)),
      slice2(5),
      slice2(6),
      slice2(7),
      toFloatPropio(slice2(8)),
      toFloatPropio(slice2(9)),
      toFloatPropio(slice2(10)),
      toIntPropio(slice2(11)),
      slice2(12),
      toIntPropio(slice2(13)),
      toIntPropio(slice2(14)),
      toIntPropio(slice2(15)),
      toIntPropio(slice2(16)),
      toIntPropio(slice2(17)),
      toIntPropio(slice2(18)),
      toIntPropio(slice2(19)),
      toIntPropio(slice2(20))
    )

    segundaParte
  }

  def procesar_tercera_parte(lista: List[String]): TerceraParte = {
    val slice3 = lista.slice(42, 64)
    println(slice3)
    val terceraParte = TerceraParte(
      slice3(0),
      slice3(1),
      slice3(2),
      slice3(3),
      toIntPropio(slice3(4)),
      slice3(5),
      slice3(6),
      slice3(7),
      toIntPropio(slice3(8)),
      toIntPropio(slice3(9)),
      toIntPropio(slice3(10)),
      toIntPropio(slice3(11)),
      slice3(12),
      slice3(13),
      slice3(14),
      slice3(15),
      toIntPropio(slice3(16)),
      toIntPropio(slice3(17)),
      slice3(18),
      toIntPropio(slice3(19)),
      toIntPropio(slice3(20)),
      slice3(21)
    )

    terceraParte
  }

  def procesar_cuarta_parte(lista: List[String]): CuartaParte = {
    val slice4 = lista.slice(64, 85)
    println(slice4)
    val cuarteParte = CuartaParte(
      slice4(0),
      slice4(1),
      toIntPropio(slice4(2)),
      toIntPropio(slice4(3)),
      toFloatPropio(slice4(4)),
      toFloatPropio(slice4(5)),
      toIntPropio(slice4(6)),
      toIntPropio(slice4(7)),
      toIntPropio(slice4(8)),
      toIntPropio(slice4(9)),
      toIntPropio(slice4(10)),
      toIntPropio(slice4(11)),
      toIntPropio(slice4(12)),
      toIntPropio(slice4(13)),
      toIntPropio(slice4(14)),
      toIntPropio(slice4(15)),
      toIntPropio(slice4(16)),
      toIntPropio(slice4(17)),
      toIntPropio(slice4(18)),
      toIntPropio(slice4(19)),
      slice4(20)
    )

    cuarteParte
  }

  def procesar_quinta_parte(lista: List[String]): QuintaParte = {
    val slice5 = lista.slice(85, 92)
    println(slice5)
    val quintaParte = QuintaParte(
      toIntPropio(slice5(0)),
      slice5(1).replace(" u ", "").replace(" \'"," \"").patch(0,"\'",0).concat("'") ,
      toFloatPropio(slice5(2)),
      toIntPropio(slice5(3)),
      toFloatPropio(slice5(4)),
      toFloatPropio(slice5(5)),
      toFloatPropio(slice5(6))
    )

    quintaParte
  }

  def insertar(primeraParte: PrimeraParte,
               segundaParte: SegundaParte,
               terceraParte: TerceraParte,
               cuartaParte: CuartaParte,
               quintaParte: QuintaParte): Update0 =
    sql"""
    insert
    	into
    		data( maiScore,
    		deviceMatch,
    		factorCodes,
    		firstEncounter,
    		icAddress,
    		icInternet,
    		icSuspicious,
    		icVelocity,
    		icIdentity,
    		ipRoutingMethod,
    		reasonCode,
    		timeOnPage,
    		billinCountryCode,
    		cancelled,
    		cardContryCode,
    		pp_1,
    		pp_30,
    		pp_60,
    		pp_90,
    		caseDate,
    		caseMinuteDistance,
    		casesCount,
    		channel,
    		correlID,
    		countDifferentCards,
    		countDifferentInstallments,
    		countryCode,
    		countryFrom,
    		countryTo,
    		distanceToArrival,
    		distanceToDeparture,
    		domainProc,
    		maiAdvice,
    		maiVerification,
    		maiReason,
    		maiRisk,
    		maibisScore,
    		maiStatus,
    		maiUnique,
    		maiAvgSecs,
    		maiBuys,
    		maySearches,
    		eulerBadge,
    		maiPax,
    		maiType,
    		maiRels,
    		maiApp,
    		mai_urgency,
    		mai_network,
    		mai_all_pax,
    		maiLastSeconds,
    		apocrypha,
    		friendly,
    		hours_since_last_verification,
    		iataFrom,
    		iataTo,
    		ip_city,
    		mai_language,
    		mai_negative,
    		mai_suspect,
    		mai_os,
    		mai_policy_score,
    		mai_pulevel,
    		maibis_reason,
    		mai_city,
    		mai_region,
    		maitris_score,
    		lag_time_hours,
    		many_holders_for_card,
    		many_name_for_document,
    		online_airport_state,
    		online_billing_address,
    		online_cep_number_bond,
    		online_city_bond,
    		online_ddd,
    		online_ddd_bond,
    		online_death,
    		online_email,
    		online_family_bond,
    		online_ip_state,
    		online_name,
    		online_phone,
    		online_queries,
    		online_state_bond,
    		payment_card_type,
    		payment_installments,
    		same_field_features,
    		speed_to_departure,
    		total_usd_amounts,
    		triangulation_height,
    		triangulation_height_speed,
    		trip_distance )
    	values ( ${primeraParte.maiScore},
        ${primeraParte.deviceMatch},
        ${primeraParte.factorCodes},
        ${primeraParte.firstEncounter},
        ${primeraParte.icAddress},
        ${primeraParte.icInternet},
        ${primeraParte.icSuspicious},
        ${primeraParte.icVelocity},
        ${primeraParte.icIdentity},
        ${primeraParte.ipRoutingMethod},
        ${primeraParte.reasonCode},
        ${primeraParte.timeOnPage},
        ${primeraParte.billinCountryCode},
        ${primeraParte.cancelled},
        ${primeraParte.cardContryCode},
        ${primeraParte.pp_1},
        ${primeraParte.pp_30},
        ${primeraParte.pp_60},
        ${primeraParte.pp_90},
        ${primeraParte.caseDate},
        ${primeraParte.caseMinuteDistance},
        ${segundaParte.casesCount},
        ${segundaParte.channel},
        ${segundaParte.correlID},
        ${segundaParte.countDifferentCards},
        ${segundaParte.countDifferentInstallments},
        ${segundaParte.countryCode},
        ${segundaParte.countryFrom},
        ${segundaParte.countryTo},
        ${segundaParte.distanceToArrival},
        ${segundaParte.distanceToDeparture},
        ${segundaParte.domainProc},
        ${segundaParte.maiAdvice},
        ${segundaParte.maiVerification},
        ${segundaParte.maiReason},
        ${segundaParte.maiRisk},
        ${segundaParte.maibisScore},
        ${segundaParte.maiStatus},
        ${segundaParte.maiUnique},
        ${segundaParte.maiAvgSecs},
        ${segundaParte.maiBuys},
        ${segundaParte.maySearches},
        ${terceraParte.eulerBadge},
        ${terceraParte.maiPax},
        ${terceraParte.maiType},
        ${terceraParte.maiRels},
        ${terceraParte.maiApp},
        ${terceraParte.mai_urgency},
        ${terceraParte.mai_network},
        ${terceraParte.mai_all_pax},
        ${terceraParte.maiLastSeconds},
        ${terceraParte.apocrypha},
        ${terceraParte.friendly},
        ${terceraParte.hours_since_last_verification},
        ${terceraParte.iataFrom},
        ${terceraParte.iataTo},
        ${terceraParte.ip_city},
        ${terceraParte.mai_language},
        ${terceraParte.mai_negative},
        ${terceraParte.mai_suspect},
        ${terceraParte.mai_os},
        ${terceraParte.mai_policy_score},
        ${terceraParte.mai_pulevel},
        ${terceraParte.maibis_reason},
        ${cuartaParte.mai_city},
        ${cuartaParte.mai_region},
        ${cuartaParte.maitris_score},
        ${cuartaParte.lag_time_hours},
        ${cuartaParte.many_holders_for_card},
        ${cuartaParte.many_name_for_document},
        ${cuartaParte.online_airport_state},
        ${cuartaParte.online_billing_address},
        ${cuartaParte.online_cep_number_bond},
        ${cuartaParte.online_city_bond},
        ${cuartaParte.online_ddd},
        ${cuartaParte.online_ddd_bond},
        ${cuartaParte.online_death},
        ${cuartaParte.online_email},
        ${cuartaParte.online_family_bond},
        ${cuartaParte.online_ip_state},
        ${cuartaParte.online_name},
        ${cuartaParte.online_phone},
        ${cuartaParte.online_queries},
        ${cuartaParte.online_state_bond},
        ${cuartaParte.payment_card_type},
        ${quintaParte.payment_installments},
        ${quintaParte.same_field_features},
        ${quintaParte.speed_to_departure},
        ${quintaParte.total_usd_amounts},
        ${quintaParte.triangulation_height},
        ${quintaParte.triangulation_height_speed},
        ${quintaParte.trip_distance}
     )
    """.update


  def cargar_db(lista: List[String]): Either[String, String] = {
    insertar(procesar_primera_parte(lista),
             procesar_segunda_parte(lista),
             procesar_tercera_parte(lista),
             procesar_cuarta_parte(lista),
             procesar_quinta_parte(lista)).run.transact(conexion).unsafeRunSync match {
               case 1 => Right("Success")
               case _ => Left("Failed")
             }

  }

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

  crearTable.run.transact(conexion).unsafeRunSync
  iterador(reader.next())

}
