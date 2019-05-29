Sealed trait Free[S[+_],+A] {
  private case class FlatMap[S[+_], A, B] (a : Free[S,A], f : A => Free[S,B]) extends Free[S,B]
  case class Done[S[+_], +A] (a : A) extends Free[S,A]
  case class More[S[+_], +A] (k : S[Free[S,A]]) extends Free[S,A]

}

// For comprenhension

val maybeFullName = maybeFirstName.flatMap{firstName => maybeLastName.map{ lastName => firstName + " " + lastName}}

val maybeFullName = for {
  firstName <- maybeFirstName
  lastName <- maybeLastName
} yield firstName + " " + lastName
