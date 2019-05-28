// Trampolines

// Scala solo soporta recursion de cola (o al menos es la unica forma que lo optimiza
// para no llenar el stack)

// Vamos a describir una iteracion con un ADT

Sealed Trait Trampoline[+A] {
  final def resume : Either[ () => Trampoline[A], A] =
    this match {
      case Done(d) => Right(d)
      case More(k) => Left(k)
      case FlatMap(sub, cont) => sub match {
        case Done(d2) => cont(d2).resume()
        case More(k2) => Left( () =>  FlatMap(k2(), cont))
        case FlatMap(sub2, cont2) =>  FlatMap(sub2, x => FlatMap(cont2(x), cont)).resume()
      }
    }

  final def runT : A = {
    this.resume() match {
      case Right(d) => d
      case Left(k) => k().runT()

    }
  }
  def flatMap(f : A => Trampoline[B]) : Trampoline[B] = {
    this match {
      case FlatMap(sub, cont) => FlatMap(sub, x => cont(x).flatMap(f))
      case x => FlatMap(x,f)
    }

  }
}

case class Done[+A](d : A) extends Trampoline[A]
case class More[+A](call : () => Trampoline[A]) extends Trampoline[A] // La 0-funcion es CLAVE
private case class FlatMap[+A, +B](sub : Trampoline[A], cont : A => Trampoline[A])

// Monada libre es la minima estructura de monada sobre un ADT que tenga un flatmap
// Pensar que es el free de una funcion unaria
