package spfsfiuba.model

import cats.effect.Sync
import io.circe.generic.semiauto.deriveDecoder
import io.circe.Decoder
import org.http4s.circe.jsonOf
import org.http4s.EntityDecoder

case class Input(
    val mai_score: Option[Double],
    val DeviceMatch: Option[Double],
    val FactorCodes: Option[Double],
    val FirstEncounter: Option[Double],
    val IcAddress: Option[Double],
    val IcInternet: Option[Double],
    val IcSuspicious: Option[Double],
    val IcVelocity: Option[Double],
    val Icidentity: Option[Double],
    val IpRoutingMethod: Option[Double],
    val ReasonCode: Option[Double],
    val TimeOnPage: Option[String],
    val BillingCountryCode: Option[String],
    val cancelled: Option[Int],
    val cardCountryCode: Option[String],
    val pp_1: Option[Int],
    val pp_30: Option[Int],
    val pp_60: Option[Int],
    val pp_90: Option[Int],
    val caseDate: Option[String],
    val case_minutes_distance: Option[String],
    val cases_count: Option[Int],
    val channel: Option[String],
    val correl_id: Option[Int],
    val count_different_cards: Option[String],
    val count_different_installments: Option[String],
    val countryCode: Option[String],
    val countryFrom: Option[String],
    val countryTo: Option[String],
    val distance_to_arrival: Option[Int],
    val distance_to_departure: Option[Double],
    val domain_proc: Option[Double],
    val mai_advice: Option[Double],
    val mai_verification: Option[String],
    val mai_reason: Option[Int],
    val mai_risk: Option[Int],
    val maibis_score: Option[Int],
    val mai_status: Option[Int],
    val mai_unique: Option[Int],
    val mai_avg_secs: Option[String],
    val mai_buys: Option[String],
    val mai_searches: Option[String],
    val eulerBadge: Option[String],
    val mai_pax: Option[String],
    val mai_type: Option[String],
    val mai_rels: Option[String],
    val mai_app: Option[Int],
    val mai_urgency: Option[String],
    val mai_network: Option[String],
    val mai_all_pax: Option[String],
    val mai_last_secs: Option[String],
    val apocrypha: Option[Int],
    val friendly: Option[Int],
    val hours_since_first_verification: Option[Int],
    val iataFrom: Option[String],
    val iataTo: Option[String],
    val ip_city: Option[String],
    val mai_language: Option[String],
    val mai_negative: Option[Int],
    val mai_suspect: Option[Int],
    val mai_os: Option[String],
    val mai_policy_score: Option[Int] ,
    val mai_pulevel: Option[Int],
    val maibis_reason: Option[String],
    val mai_city: Option[String],
    val mai_region: Option[String],
    val maitris_score: Option[Int],
    val lagTimeHours: Option[Int],
    val many_holders_for_card: Option[String],
    val many_names_for_document: Option[String],
    val online_airport_state: Option[Int],
    val online_billing_address_state: Option[Int],
    val online_cep_number_bond: Option[Int],
    val online_city_bond: Option[Int],
    val online_ddd: Option[Int],
    val online_ddd_bond: Option[Int],
    val online_death: Option[Int],
    val online_email: Option[Int],
    val online_family_bond: Option[Int],
    val online_ip_state: Option[Int],
    val online_name: Option[Int],
    val online_phone: Option[Int],
    val online_queries: Option[Int],
    val online_state_bond: Option[Int],
    val paymentsCardType: Option[String],
    val paymentsInstallments: Option[Int],
    val same_field_features: Option[String],
    val speed_to_departure: Option[Double],
    val totalUsdAmount: Option[Int],
    val triangulation_height: Option[Double],
    val triangulation_height_speed: Option[Double],
    val trip_distance: Option[Double]
)


object Input {
    implicit val InputDecoder: Decoder[Input] = deriveDecoder[Input]
    implicit def InputEntityDecoder[F[_]: Sync]: EntityDecoder[F, Input] = jsonOf
    implicit val inputCodec: HeaderCodec[Input] = HeaderCodec.caseCodec("mai_score"
    ,"DeviceMatch"
    ,"FactorCodes"
    ,"FirstEncounter"
    ,"IcAddress"
    ,"IcInternet"
    ,"IcSuspicious"
    ,"IcVelocity"
    ,"Icidentity"
    ,"IpRoutingMethod"
    ,"ReasonCode"
    ,"TimeOnPage"
    ,"BillingCountryCode"
    ,"cancelled"
    ,"cardCountryCode"
    ,"pp_1"
    ,"pp_30"
    ,"pp_60"
    ,"pp_90"
    ,"caseDate"
    ,"case_minutes_distance"
    ,"cases_count"
    ,"channel"
    ,"correl_id"
    ,"count_different_cards"
    ,"count_different_installments"
    ,"countryCode"
    ,"countryFrom"
    ,"countryTo"
    ,"distance_to_arrival"
    ,"distance_to_departure"
    ,"domain_proc"
    ,"mai_advice"
    ,"mai_verification"
    ,"mai_reason"
    ,"mai_risk"
    ,"maibis_score"
    ,"mai_status"
    ,"mai_unique"
    ,"mai_avg_secs"
    ,"mai_buys"
    ,"mai_searches"
    ,"eulerBadge"
    ,"mai_pax"
    ,"mai_type"
    ,"mai_rels"
    ,"mai_app"
    ,"mai_urgency"
    ,"mai_network"
    ,"mai_all_pax"
    ,"mai_last_secs"
    ,"apocrypha"
    ,"friendly"
    ,"hours_since_first_verification"
    ,"iataFrom"
    ,"iataTo"
    ,"ip_city"
    ,"mai_language"
    ,"mai_negative"
    ,"mai_suspect"
    ,"mai_os"
    ,"mai_policy_score"
    ,"mai_pulevel"
    ,"maibis_reason"
    ,"mai_city"
    ,"mai_region"
    ,"maitris_score"
    ,"lagTimeHours"
    ,"many_holders_for_card"
    ,"many_names_for_document"
    ,"online_airport_state"
    ,"online_billing_address_state"
    ,"online_cep_number_bond"
    ,"online_city_bond"
    ,"online_ddd"
    ,"online_ddd_bond"
    ,"online_death"
    ,"online_email"
    ,"online_family_bond"
    ,"online_ip_state"
    ,"online_name"
    ,"online_phone"
    ,"online_queries"
    ,"online_state_bond"
    ,"paymentsCardType"
    ,"paymentsInstallments"
    ,"same_field_features"
    ,"speed_to_departure"
    ,"totalUsdAmount"
    ,"triangulation_height"
    ,"triangulation_height_speed"
    ,"trip_distance")(Input.apply _)(Input.unapply _)
    def toMap(input: Input) : Map[String, Any] = input.getClass.getDeclaredFields
      .map( _.getName ) // all field names
      .zip(input.productIterator.to) // build a list with tuples (fieldName, fieldValue)
      .toMap // convert it to a map
}
