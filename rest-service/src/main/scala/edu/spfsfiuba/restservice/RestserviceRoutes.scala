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
      case req @ POST -> Root/ "predict" =>
        for {
          request <- req.as[Json]
          result <- P.get(request)
          response <- Ok(result)
        } yield response
    }
  }
}
