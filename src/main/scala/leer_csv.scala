import java.io.File
import kantan.csv._         // All kantan.csv types.
import kantan.csv.ops._     // Enriches types with useful methods.

object leerCSV {
  final case class Fila(maiScore : Int, deviceMatch : Int, factorCodes : Int,
                        firstEncounter : Int, icAddress : Int, icInternet : Int,
                        icSuspicious : Int, icVelocity : Int, icIdentity : Int,
                        ipRoutingMethod : Int, reasonCode : Int, timeOnPage : Int,
                        billinCountryCode : String, cancelled : Int, cardContryCode : String,
                        pp_1 : Int, pp_30 : Int, pp_60 : Int, pp_90 : Int, caseDate : String, // caseDate es una fecha, hay que ver si Scala o Java tiene un tipo Date que soporte ese formato
                        caseMinuteDistance : Int, casesCount : Int, channel : String,
                        correlID : Int, countDifferentCards : Int, countDifferentInstallments : Int,
                        countryCode : String, countryFrom : String, countryTo : String,
                        distanceToArrival : Float, distanceToDeparture : Float, domainProc : Float,
                        maiAdvice : Int, maiVerification : String, maiReason : Int, // maiVerification es otra fecha, idem caseDate
                        maiRisk : Int, maibisScore : Int, maiStatus : Int, maiUnique : Int,
                        maiAvgSecs : Int, maiBuys : Int, maySearches : Int, eulerBadge : String, // eulerBadge es una especie de enum creo, ya que solamente veo 3 strings como valores
                        maiPax : String, maiType : String, maiRels : String, maiApp : Int, // maiPax es un bar separated values ¯\_(ツ)_/¯
                        maiUrgency : String, maiNetwork : String, maiAllPAx : Int,
                        maiLastSeconds : Int, ) extends Any {

  }
}
