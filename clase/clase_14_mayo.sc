Sealed Trait Option[+A] {
  def map[B](f : A => B) : Option[B] = this.flatMap(a : A => Option.pure(f(a)))
  def flatMap[B](f : A => Option[B]) : Option[B]
}

case object None extends Option[Nothing] {
  override def flatMap[A] (f : A => Option[B] ) : Option[B] = this
}
case class Some[+A] (d : A) extends Option[A] {
  override def flatMap[A](f : A => Option[B]) : Option[B] = f(a)
}

// Chequear que flatMap es asociativo

object Option {
  def lift[A](d : A) : Option[A] = Some(d)
}
