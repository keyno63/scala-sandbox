package jp.co.who.monad

object Continue {
  def pure[R, A](a: A): Continue[R, A] =
    Continue(ar => ar(a))
}

final case class Continue[R, A](run:(A=>R)=>R) {
  def flatMap[B](f: A => Continue[R, B]): Continue[R, B] = {
    Continue(br => run(a => f(a).run(br)))
  }

  def map[B](f: A => B): Continue[R, B] =
    flatMap(a => Continue.pure(f(a)))
}

object Count extends App {
  def fizzCont(i: Int): Continue[String, Int] = Continue { cont =>
    if (i % 3 == 0) "Fizz"
    else cont(i)
  }
  def buzzCont(i: Int): Continue[String, Int] = Continue { cont =>
    if (i % 5 == 0) "Buzz"
    else cont(i)
  }
  def fizzBuzzCont(i: Int): Continue[String, Int] = Continue {cont =>
    if (i % 15== 0) "FizzBuzz"
    else cont(i)
  }
  def fizzBuzz(i: Int): Continue[String, Int] = {
    for {
      a <- fizzBuzzCont(i)
      b <- fizzCont(a)
      c <- buzzCont(b)
    } yield c
  }
  val ret = (1 to 20).map(fizzBuzz(_).run(_.toString)).take(15).toList
  println(ret)
}

