package edu.spfsfiuba.restservice

import java.io.File

import cats.Applicative
import cats.effect.Sync
import cats.implicits._
import io.circe.{Decoder, Encoder, HCursor, Json}
import io.circe.generic.semiauto._
import org.http4s.{EntityDecoder, EntityEncoder, Method, Request, Uri}
import org.http4s.circe._
import edu.spfsfiuba.dbloader.SqlHelper
import org.dmg.pmml.FieldName
import org.jpmml.evaluator.{Evaluator, EvaluatorUtil, InputField, LoadingModelEvaluatorBuilder}
import scala.collection.JavaConverters._
import spfsfiuba.model._

trait Predict[F[_]] {
  def get(input: Input): F[Predict.Apocrypha]
}

object Predict {
  implicit def apply[F[_]](implicit ev: Predict[F]): Predict[F] = ev

  final case class Apocrypha(apocrypha: Int) extends AnyVal
  
  object Apocrypha {
    implicit val apocryphaDecoder: Decoder[Apocrypha] =
      deriveDecoder[Apocrypha]
    implicit def apocryphaEntityDecoder[F[_]: Sync]
      : EntityDecoder[F, Apocrypha] = jsonOf

    implicit val apocryphaEncoder: Encoder[Apocrypha] = new Encoder[Apocrypha] {
      final def apply(a: Apocrypha): Json = Json.obj(
        ("result", Json.fromInt(a.apocrypha)),
      )
    }
      deriveEncoder[Apocrypha]
    implicit def apocryphaEntityEncoder[F[_]: Applicative]
      : EntityEncoder[F, Apocrypha] = jsonEncoderOf
  }

  final case class ApocryphaError(e: Throwable) extends RuntimeException

  def impl[F[_]: Applicative]: Predict[F] = new Predict[F]{
    def get(input : Input): F[Apocrypha] = {
      val dataMap: Map[String, Any] = Input.toMap(input)
      dataMap.get("correl_id") match {
        case Some(value) => {
          val correl_id: Int = value.toString.toInt
          SqlHelper.getApocryphaFromDB(correl_id) match {
            case Right(dato: Int) => Predict.Apocrypha(dato).pure[F]
            case Left(f: String) => Predict.Apocrypha(predictFromModel(dataMap)).pure[F]
          }
        }
        case None => throw new Exception("correl_id is required") // TODO: Proper error handling
      }
    }

    def predictFromModel(dataMap: Map[String, Any]): Int = {
      val evaluator: Evaluator = new LoadingModelEvaluatorBuilder()
        .load(new File("/opt/docker/output/model.pmml"))
        .build()

      val activeFields: List[InputField] = evaluator.getActiveFields.asScala.toList

      val arguments = activeFields.map(field => {
        val name: FieldName = field.getName
        val rawValue = dataMap.getOrElse(name.getValue,0)

        val value = field.prepare(rawValue)
        // Transforming an arbitrary user-supplied value to a known-good PMML value
        (name, value)
      }).toMap.asJava

      val results = EvaluatorUtil.decodeAll(evaluator.evaluate(arguments))
      val prediction: Int = results.get("apocrypha").toString.toInt // TODO: Nasty hack to make it work
      prediction
    }
  }
}
