import java.io.File
import cats.effect._
import kantan.csv._ // All kantan.csv types.
import kantan.csv.ops._ // Enriches types with useful methods.
import kantan.codecs.resource.ResourceIterator


object leerCSV extends App {

  val data = new File("assets/csv/train.csv")

  val reader = data.asCsvReader[List[String]](rfc.withHeader)

  // def cargar_db(reader: ResourceIterator[List[String]], session: Resource[IO, SessionIO]): Either[String, String] = {
  def iterador(lista: ReadResult[List[String]]) : Either[String, String] ={
    println("Entre")
    lista match {
      case Right(l) => { println(l)
                         iterador(reader.next())}
      case Left(k) => Left("Termino el archivo")
    }
  }

  iterador(reader.next())

}
