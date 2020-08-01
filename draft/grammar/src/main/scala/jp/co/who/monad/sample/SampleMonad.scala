package jp.co.who.monad.sample

object SampleMonad {
  def apply[A](id: A): SampleMonad[A] = new SampleMonad(id)
}

class SampleMonad[A](id: A) {

  def map[B](f: A => B): SampleMonad[B] = {
    val a: B = f(this.id)
    SampleMonad[B](a)
  }

  def flatMap[B](f: A => SampleMonad[B]): SampleMonad[B] = {
    f(this.id)
  }

  override def toString: String = id.toString

}
