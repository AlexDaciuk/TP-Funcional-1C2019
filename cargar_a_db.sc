import cats.effect._
import skunk._
import skunk.implicits._
import skunk.codec.numeric._

val session: Resource[IO, SessionIO] =
  Session.single(
    host = "localhost",
    port = 5432,
    user = "funcional",
    password = "funcional",
    database = "funcional"
  )
