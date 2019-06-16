package edu.spfsfiuba.apitest

import org.http4s.client.blaze._
import org.http4s.client._
import scala.concurrent.ExecutionContext.Implicits.global
import java.util.concurrent._
import cats._
import cats.data._
import cats.effect._

object ApiTest extends App {
  val blockingEC = ExecutionContext.fromExecutorService(Executors.newFixedThreadPool(5))
  val httpClient: Client[IO] = JavaNetClientBuilder[IO](blockingEC).create

  // Leo el CSV de test
  val data = new File("input/csv/test.csv")
  val reader = data.asCsvReader[List[String]](rfc.withHeader)

  def consultar(fila: Json): Int = {
    val request = Uri.uri("http://localhost:8080/predict/") / fila
    httpClient.expect[Int](request).unsafeRunSync
  }

  def iterador(lista: ReadResult[List[String]]): String = {
    lista match {
      case Right(l) =>
        consultar(l.asJson) match {
          case Int(a) => {
            println(s"Apocrypha vale : $a")
            iterador(reader.next())
          }
          case _ => println("No tendria que haber llegado aca.")
        }
      case Left(k) => println("Termino el CSV")
    }
  }

  iterador(reader.next())

}
