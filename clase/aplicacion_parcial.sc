object Parc {

  val f = (x: Int, y: Int) => x + y

  val c: Int => Int = { case (x: Int) => x + 4 }

  val d = new Function1[Int, Int] {
    def apply(x: Int) = x + 4
  }

}
