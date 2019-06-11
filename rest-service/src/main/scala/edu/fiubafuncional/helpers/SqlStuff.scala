package edu.fiubafuncional.helpers

import doobie._
import doobie.implicits._
import doobie.util.ExecutionContexts
import cats._
import cats.data._
import cats.effect._

object SqlStuff {
  // Me conecto a la db
  implicit val cs = IO.contextShift(ExecutionContexts.synchronous)

  val conexion = Transactor.fromDriverManager[IO](
    "org.postgresql.Driver", // driver classname
    "jdbc:postgresql:funcional", // connect URL (driver-specific)
    "funcional", // user
    "", // password
    ExecutionContexts.synchronous // just for testing
  )

  def getFromDB(correl_id : Int) : Either[String, Int] = {
    query = sql"""
    select apocrypha from data where correl_id = $correl_id
    """.query[Int]

    query.stream.compile.toList.transact(conexion).unsafeRunSync match {
      case apocrypha :: Nil => Right(apocrypha)
      case Nil => Left("No existe ese registro en la db")
      case _ => Left("Esto no deberia pasar")
    }
  }
}
