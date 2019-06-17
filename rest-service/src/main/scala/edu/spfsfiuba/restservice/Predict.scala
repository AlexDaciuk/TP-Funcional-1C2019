package edu.spfsfiuba.restservice

import cats.Applicative
import cats.effect.Sync
import cats.implicits._
import io.circe.{Encoder, Decoder, Json, HCursor}
import io.circe.generic.semiauto._
import org.http4s.{EntityDecoder, EntityEncoder, Method, Uri, Request}
import org.http4s.client.Client
import org.http4s.client.dsl.Http4sClientDsl
import org.http4s.Method._
import org.http4s.circe._
import edu.spfsfiuba.dbloader.SqlHelper

trait Predict[F[_]] {
  def get(data: Json): F[Predict.Apocrypha]
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
    def get(data : Json): F[Apocrypha] = {

      val dataL: List[String] = data.as[List[String]] match {
        case Right(l) => l
        case Left(f) => List[String]()
      }

      SqlHelper.getApocryphaFromDB(dataL(23).toInt) match {
        case Right(dato: Int) => Predict.Apocrypha(dato).pure[F]
        case Left(f: String) => Predict.Apocrypha(predictFromModel(dataL)).pure[F]
      }
    }

    def predictFromModel(list: List[String]): Int = 2 // TODO: Fix this
  }
}
