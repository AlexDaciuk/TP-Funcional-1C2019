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
  val data : File = new File("input/csv/test.csv")

  val reader = data.asCsvReader[List[String]](rfc)

  val header = getHeaders(reader.next())

  val url_rest : String = scala.util.Properties.envOrElse("REST_URL", "localhost")

  def getHeaders(lista: ReadResult[List[String]]) : List[String] = {
    lista match {
      case Right(l) => l
      case _ => List[String]()
    }
  }

  println(header)

  def consultar(fila: Json): Stream[IO, Int] = {
    val req = POST(fila, Uri.uri("http://rest-url:8080/predict"))
    BlazeClientBuilder[IO](global).stream.flatMap {httpClient =>
      // Decode response
      Stream.eval(httpClient.expect(req)(jsonOf[IO, Int]))
    }
  }

  def iterador(lista: ReadResult[List[String]]): Unit = {
   lista match {
     case Right(l) => {
        val tuplas_tmp : List[(String, String)] = header zip l
        consultar((tuplas_tmp.toMap - "same_field_features").filter((t) => t._2.nonEmpty).asJson).compile.last.unsafeRunSync
        iterador(reader.next())}
     case Left(k) => println("Termino el CSV")
   }
  }
  println(url_rest)
  iterador(reader.next())
}
