package jp.co.who.monad.eitherT

import cats.data.EitherT
import cats.implicits._
import jp.co.who.monad.eitherT.EitherTSample.DivideError.{ Indivisible, ZeroDivision }

import scala.concurrent.{ duration, Await, Future }
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

object EitherTSample extends App {

  val ret = divideAsyncEitherThreeTimes(10, 2)
  ret.onComplete {
    case Success(value) =>
      value match {
        case Right(data)             => println(s"data: $data")
        case Left(Indivisible(n, d)) => println(s"$n is not divisible by $d")
        case Left(ZeroDivision)      => println("denom must not be 0")
      }
    case Failure(exception) => exception.printStackTrace()
  }
  Await.ready(ret, duration.Duration.Inf)

  def divideAsyncEitherThreeTimes(num: Int, denom: Int): Future[Either[DivideError, Int]] = {
    val e = for {
      r1 <- EitherT(divideAsyncEither(num, denom))
      r2 <- EitherT(divideAsyncEither(r1, denom))
      r3 <- EitherT(divideAsyncEither(r2, denom))
    } yield r3
    e.value
  }

  def divideAsyncEither(num: Int, denom: Int): Future[Either[DivideError, Int]] =
    Future(divideEither(num, denom))

  def divideEither(num: Int, denom: Int): Either[DivideError, Int] =
    if (denom == 0) Left(ZeroDivision)
    else if (num % denom != 0) Left(Indivisible(num, denom))
    else Right(num / denom)

  // Defined Either Error
  sealed trait DivideError
  object DivideError {
    case class Indivisible(num: Int, denom: Int) extends DivideError
    object ZeroDivision                          extends DivideError
  }
}

// TODO: use later.
case class User(id: Int, name: String)

trait UserRepository {
  def findBy(userId: Long): Future[Option[User]]
}

object UserRepository {
  trait FindByError
  object FindByError {
    case object UserNotFound extends FindByError
  }
}
