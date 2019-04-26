sealed trait SmartStream[+A]
case object Nil extends SmartStream[Nothing]
case class Cons[+A](h : () => A, t : () => SmartStream[A]) extends SmartStream[A]

object SmartStream {
  def nil[A] : SmartStream[A] = Nil

  def cons[A](h : => A, t : => SmartStream[A]) : SmartStream[A] = {
    lazy val g = h
    lazy val u = t
    Cons(() => g, () => u)
  }

  def apply[A](as: A*) : SmartStream[A] = {
    if (as.isEmpty) nil
    else cons( as.head, apply(as.tail: _*))
  }

  def exists[A]( p : A => Boolean) : Boolean = {
    this match {
      case Cons(h, t) => p(h()) || t().exists(p)
      }
    }

  def sum( s : SmartStream[Int]) : Int = s match {
    case Nil => 0
    case Cons(h, t) => h() + sum(t())
  }

  def product( s : SmartStream[Double]) : Double = s match {
    case Nil => 1
    case Cons(h, t) => h() * product(t())
  }

  def foldRight[A,B](l : SmartStream[A])(z : B)(f : (A,B) => B) : B = l match {
    case Nil => z
    case Cons(h, t) => f(h, foldRight(t)(z)(f))
  }

  @tailrecursive
  def foldLeft[A,B](l : SmartStream[A])(z : B)(f : (A,B) => B) : B = l match {
    case Nil => z
    case Cons(h, t) => foldLeft(t)(f(h,z))(f)
  }

  def drop_while( elem : A, s : SmartStream[A]) : SmartStream[A] = {
    
  }
}
