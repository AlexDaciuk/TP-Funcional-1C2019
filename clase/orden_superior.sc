object OS {

  def pimba(n: Int): Int = {
    return n * 2
  }

  val f = (num: Int, func: Int => Int) => func(num)

  val g = (n: Int) => (m: Int) => m * n * 2

  val c = (n: Int) => (func: Int => Int) => func(n)

}
