// Functor manda objetos de una categoria a otra
// Manda identidad a identidad y composicion a composicion

Sealed Trait Option[+A] {
  def map[B](f : A => B) : Option[B] = this.flatMap(a : A => Option.lift(f(a)))
  def flatMap[B](f : A => Option[B]) : Option[B]
}

case object None extends Option[Nothing] {
  override def flatMap[A] (f : A => Option[B] ) : Option[B] = this
  override def map[B] (f : A => B) : Option[B] = this

}
case class Some[+A] (d : A) extends Option[A] {
  override def flatMap[A](f : A => Option[B]) : Option[B] = f(d)
  override def map[B] (f : A => B) : Option[B] = Some(f(d))
}

// Chequear que flatMap es asociativo

object Option {
  def lift[A](d : A) : Option[A] = Some(d)

  def product[A,B] (fa : Option[A], fb : Option[B]) : Option[A,B] = {
    fa.flatMap(a => fb.flatMap(b => Option.lift(a,b)))
  }

  def map2[A,B,C] (a : Option[A])(b : Option[B])(f : (A,B) => C) : Option[C] = {
    product[a,b].map(f)
  }
}

// Aplicativo (apply)
// Functor que tiene un pure y un map 2
// Todo functor con estructura de monada tiene estructura de aplicativo
// Pero no necesariamente un aplicativo tiene estructura de monada

// Tener map2 y apply es equivalente
// lift + map2 = map => apply
// lift + apply = map => map2

def map[B] (f : A => B) : Option[B] = {
  Option.apply[A,B](Option.lift(f))(this)
}

// Apply en terminos de map2

def apply[A,B](f : Option[A=>B])(fa : Option[A]) = {
  map2((x,y)=>x(y))(f,fa)
}

// Map2 en terminos de Apply
def map2[A,B,C](fa : Option[A], fb : Option[B])(f : (A,B) => C) : Option[C] ={
  Option.apply(Option.pure(f))(Option.product(fa,fb))
}

// Pero product usa flatMap, hay que re-escribirlo SIN flatMap

def product[A,B](fa Option[A], fb : Option[B]) : Option[A,B] = {
  Option.apply(fa.map(a => (b : B) => (a,b)))(fb)
}
