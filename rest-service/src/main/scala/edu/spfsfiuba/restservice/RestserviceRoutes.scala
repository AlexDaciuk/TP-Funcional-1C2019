package edu.spfsfiuba.restservice

import cats.effect.Sync
import cats.implicits._
import io.circe.Json
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl
import org.http4s.HttpRoutes

object RestserviceRoutes {
  def predictRoutes[F[_]: Sync](P: Predict[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root/ "predict" / data =>
        for {
          result <- P.getApocrypha(data)
          response <- Ok(result)
        } yield response
    }
  }
}
