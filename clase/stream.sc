sealed trait Stream[+A]
case object Nil extends Stream[Nothing]
case class Cons[+A](h : () => A, t : () => Stream[A]) extends Stream[A]

object Stream {
  def apply[A](as: A*) : Stream[A] = {
    if (as.isEmpty) Nil
    else Cons( () => as.head, () => apply(as.tail: _*))
  }
}
