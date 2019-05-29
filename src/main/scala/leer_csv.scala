import java.io.File
import kantan.csv._ // All kantan.csv types.
import kantan.csv.ops._ // Enriches types with useful methods.

object leerCSV extends App{
  val data = new File("assets/csv/train.csv")

  val reader = data.asCsvReader[List[String]](rfc.withHeader)

  val fila = List[String](reader.next())
}
