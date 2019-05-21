import cats.effect._
import skunk._
import skunk.implicits._
import skunk.codec.all._
import fs2.Stream

object Hello extends IOApp {

  // Abro la sesion, nada extraño aca, es bastante self-explanatory la sintaxsis
  // por ahora no soporta usuarios con autenticacion ¯\_(ツ)_/¯
  val session: Resource[IO, Session[IO]] =
    Session.single(
      host     = "localhost",
      port     = 5432,
      user     = "funcional",
      database = "funcional"
    )

  // Query, es un tipo, que, como dice, define querys
  // Toma [A,B], donde :
  // A es el "encoder", que son los tipos de las cosas que estas mandando
  // si haces un insert por ej, si es un select es Void
  // B es el "decoder", que son los tipos de las columnas que estas recibiendo
  // como respuesta de la query
  // Si estas insertando o recibiendo mas de un tipo, usas el "~" para indicar
  // los tipos en orden, ej: int ~ string , si recibis un numero y un varchar
  val create_table: Command[Void] =
    sql"CREATE TABLE prueba(columna1 VARCHAR(50))".command

  // Command es un tipo que define querys que no devuelven nada, por ende, solo
  // tienen encoders, por ej, para un insert que solamente inserta un VARCHAR
  // usamos String como encoder, lo mismo que para query, si estas encodeando
  // mas de un tipo, usas "~"
  val insert_falopa: Command[String] =
    sql"INSERT INTO prueba VALUES ($varchar)".command

  val select_falopa: Query[Void, String] =
    sql"SELECT * FROM prueba".query(varchar)

  // El metodo "run" es el entrypoint
  // Por ahora es medio confuso el tema
  def run(args: List[String]): IO[ExitCode] ={

  }

}
