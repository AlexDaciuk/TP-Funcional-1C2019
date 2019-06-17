package edu.spfsfiuba.apitest

import scala.concurrent.ExecutionContext.Implicits.global
import cats._
import cats.data._
import cats.syntax.all._
import cats.implicits._
import cats.effect._
import org.http4s._
import org.http4s.client._
import org.http4s.client.dsl.io._
import org.http4s.client.blaze._
import org.http4s.Uri
import org.http4s.circe._
import org.http4s.dsl.io._
import kantan.csv._ // All kantan.csv types.
import kantan.csv.ops._ // Enriches types with useful methods.
import kantan.codecs.resource.ResourceIterator
import java.io.File
import io.circe._, io.circe.generic.auto._, io.circe.parser._
import io.circe.syntax._
import fs2.Stream

object ApiTest extends App{
  implicit val cs: ContextShift[IO] = IO.contextShift(global)
  implicit val timer: Timer[IO] = IO.timer(global)

  // Leo el CSV de test
  val data = new File("../assets/csv/test.csv")
  val reader = data.asCsvReader[List[String]](rfc.withHeader)

  def consultar(fila: Json): Stream[IO, Int] = {
    val req = POST(fila, Uri.uri("http://localhost:8080/predict/"))
    BlazeClientBuilder[IO](global).stream.flatMap {httpClient =>
      // Decode response
      Stream.eval(httpClient.expect(req)(jsonOf[IO, Int]))
    }
  }

  def iterador(lista: ReadResult[List[String]]): Unit = {
   lista match {
     case Right(l) => {
       // l(86) = l(86)
      //     .replace("u'", "\"")
      //     .replace("\'", "\"")
      //     .replace("\"\"", "\"")
      //     .patch(0, "\'", 0)
      //     .concat("'")
        val l2 = l.updated(86, l(86).replace("u'", "\"")
                 .replace("\'", "\"")
                 .replace("\"\"", "\"")
                 .patch(0, "\'", 0)
                 .concat("'"))
        print(l2)
        consultar(l.asJson).compile.last.unsafeRunSync
        iterador(reader.next())}
     case Left(k) => println("Termino el CSV")
   }
  }

  iterador(reader.next())
}
