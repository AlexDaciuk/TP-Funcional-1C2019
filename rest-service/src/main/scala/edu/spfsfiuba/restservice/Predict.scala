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

trait Predict[F[_]] {
  def get: F[Predict.Apocrypha]
}

object Predict {
  final case class Apocrypha(apocrypha: Int) extends AnyVal
  object Apocrypha {
    implicit val apocryphaDecoder: Decoder[Apocrypha] =
      deriveEncoder[Apocrypha]
    implicit def apocryphaEntityDecoder[F[_]: Sync]
      : EntityDecoder[F, Apocrypha] = jsonOf

    implicit val apocryphaEncoder: Encoder[Apocrypha] =
      deriveEncoder[Apocrypha]
    implicit def apocryphaEntityEncoder[F[_]: Applicative]
      : EntityEncoder[F, Apocrypha] = jsonEncoderOf
  }

  def impl[F[_]: Sync](C: Client[F]): Predict[F] = new Predict[F]{
    val dsl = new Http4sClientDsl[F]{}
    import dsl._

    def getApocrypha(data : Json): F[Predict.Apocrypha] = {
      val dataL = data.as[List[String]] match {
        case Right(l) => l
        case Left(f) => "Algo hay que hacer cuando falla"
      }

      getFromDB(dataL(23)) match {
        case Right(dato) => Predict.Apocrypha(dato)
        case Left(f) => Predict.Apocrypha(predictFromModel(dataL))
      }
    }
  }
}
