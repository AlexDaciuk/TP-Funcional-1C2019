import scala.annotation.tailrec

// DEFINICIÓN DEL TIPO
// sealed: todas las definiciones del trait se limitan a ese archivo
// trait: es como definir interfaces
// [+A] Es un generic covariante: o sea, si B es subtipo de A, Stream[B] es subtipo de Stream[A]
sealed trait SmartStream[+A] 

// Data constructor: representa un stream vacío
case object Nil extends SmartStream[Nothing]
// Case class define otra forma de representar un Smart stream
case class Cons[+A](h : () => A, t : () => SmartStream[A]) extends SmartStream[A]

// object companion: singleton que define el comportamiento del Smart stream (por eso tiene el mismo nombre que la clase)
object SmartStream {
  def nil[A] : SmartStream[A] = Nil

  def cons[A](h : => A, t : => SmartStream[A]) : SmartStream[A] = {
    lazy val g = h
    lazy val u = t
    Cons(() => g, () => u)
  }

  // El apply es nuestro metodo para poder construir objetos de la clase de forma amigable
  // Es variadico para poder definir listas de cualquier lado (incluso vacias)
  def apply[A](as: A*) : SmartStream[A] = {
    if (as.isEmpty) nil
    else cons( as.head, apply(as.tail: _*))
  }

  def exists[A](s: SmartStream[A], p : A => Boolean) : Boolean = {
    s match {
      case Nil => false
      case Cons(h, t) => p(h()) || exists(t(),p)
    }
  }

  def sum(s: SmartStream[Int]) : Int = s match {
    case Nil => 0
    case Cons(h, t) => h() + sum(t())
  }

  def product(s: SmartStream[Int]) : Int = s match {
    case Nil => 1
    case Cons(h, t) => h() * product(t())
  }

  def foldRight[A,B](s: SmartStream[A])(z : B)(f :(A,B) => B) : B = s match {
    case Nil => z
    case Cons(h, t) => foldLeft(s)(z)(f)
  }

  @tailrec
  def foldLeft[A,B](s: SmartStream[A])(z : B)(f :(A,B) => B) : B = s match {
    case Nil => z
    case Cons(h, t) => foldLeft(t())(f(h(),z))(f)
  }

  def dropWhile[A](pred: A => Boolean, s: SmartStream[A]) : SmartStream[A] = s match {
    case Nil => Nil
    case Cons(h, t) => {
      if (!pred(h())) {
        dropWhile(pred, t())
      } else {
        Cons(h,t)
      }
    }
  }
}
