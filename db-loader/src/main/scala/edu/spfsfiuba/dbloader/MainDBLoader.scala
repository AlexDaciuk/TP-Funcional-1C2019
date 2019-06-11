package edu.spsfiuba.dbloader.LeerCSV

import java.io.File
import doobie._
import doobie.implicits._
import doobie.util.ExecutionContexts
import cats._
import cats.data._
import cats.effect._
import kantan.csv._ // All kantan.csv types.
import kantan.csv.ops._ // Enriches types with useful methods.
import kantan.codecs.resource.ResourceIterator
import fs2.Stream
import edu.spsfiuba.dbloader.SqlHelper._

object MainDBLoader extends App {
  // Abro el archivo y apunto un csvReader a ese archivo
  val data = new File("../assets/csv/train.csv")
  val reader = data.asCsvReader[List[String]](rfc.withHeader)

  def iterador(lista: ReadResult[List[String]]): Either[String, String] = {
    lista match {
      case Right(l) => {
        SqlHelper.cargar_fila(l) match {
          case Right(l) => iterador(reader.next())
          case Left(k) => Left("Fallo la carga a la db")
        }
      }
      case Left(k) => Left("Termino el archivo")
    }
  }

  SqlHelper.crearTablaDatos.run.transact(SqlHelper.conexion).unsafeRunSync
  iterador(reader.next())
  SqlHelper.crearIndiceCorrelId.run.transact(SqlHelper.conexion).unsafeRunSync


}
