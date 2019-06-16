package edu.spfsfiuba.restservice

import cats.effect.{ConcurrentEffect, Effect, ExitCode, IO, IOApp, Timer, ContextShift}
import cats.implicits._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.client.blaze.BlazeClientBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger
import fs2.Stream
import scala.concurrent.ExecutionContext.global

object RestserviceServer {

  def stream[F[_]: ConcurrentEffect](implicit T: Timer[F], C: ContextShift[F]): Stream[F, Nothing] = {
    for {
      client <- BlazeClientBuilder[F](global).stream
      helloWorldAlg = HelloWorld.impl[F]
      jokeAlg = Jokes.impl[F](client)
      predictAlg = Predict.impl[F]


      // Combine Service Routes into an HttpApp.
      // Can also be done via a Router if you
      // want to extract a segments not checked
      // in the underlying routes.
      httpApp = (
        RestserviceRoutes.helloWorldRoutes[F](helloWorldAlg) <+>
        RestserviceRoutes.jokeRoutes[F](jokeAlg) <+>
        RestserviceRoutes.predict[F](predictAlg)
      ).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      exitCode <- BlazeServerBuilder[F]
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(finalHttpApp)
        .serve
    } yield exitCode
  }.drain
}
