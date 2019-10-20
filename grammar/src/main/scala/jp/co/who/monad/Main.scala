package jp.co.who.monad

object Main extends App {

  sampleMonad()

  def sampleMonad(): Unit = {
    val xx = SampleMonad("10")
    val yy = SampleMonad(xx)
    val ll = for {
      y <- yy
      x <- y
    } yield x.toInt
    println(ll.toString)

    println(yy.map(_.toString))
  }

  def map(): Unit = {

    val val1 = Map(
      "a" -> "b",
      "b" -> "c"
    )
    val x = for {
      a <- val1.keys
      //b <- a
    } yield a
    println(x)

  }

}
