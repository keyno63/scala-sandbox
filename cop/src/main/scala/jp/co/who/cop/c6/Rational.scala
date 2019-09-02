package jp.co.who.cop.c6

class Rational(n: Int, d: Int)  {
  require(d != 0)
  private val g = gcd(n.abs, d.abs)
  println(n, g, n/g)
  private val numer =  n/g
  private val denom = d/g
  def this(n: Int) = this(n, 1)

  // numeric
  def + (i: Int): Rational =
    new Rational(
      this.numer + i * this.denom,
      this.denom
    )

  def - (i: Int): Rational =
    new Rational(
      this.numer - i * this.denom,
      this.denom
    )

  def * (i: Int): Rational =
    new Rational(
      this.numer * i,
      this.denom
    )

  def / (i: Int): Rational =
    new Rational(
      this.numer,
      this.denom * i
    )

  /**
    * (this) + (that) =
    * c/d + a/b = (bc + ad)/bd
    */
  def + (that: Rational): Rational =
    new Rational(
      this.numer * that.denom + that.numer * this.denom,
      this.denom * that.denom
    )

  /**
    * (this) - (that) =
    * c/d - a/b = (bc - ad)/bd
    */
  def - (that: Rational): Rational =
    new Rational(
      this.numer * that.denom - that.numer * this.denom,
      this.denom * that.denom
    )

  /**
    * (this) * (that) =
    * c/d * a/b = (a*c)/(b*d)
    */
  def * (that: Rational): Rational =
    new Rational(
      this.numer * that.numer,
      this.denom * that.denom
    )

  /**
    * (this) * (that) =
    * c/d / a/b = (b*c)/(a*d)
    */
  def / (that: Rational): Rational =
    new Rational(
      this.numer * that.denom,
      this.denom * that.numer
    )

  override def toString = s"$numer/$denom"
  private def gcd(i: Int, i1: Int): Int = {
    println(i, i1)
    if (i1 == 0) i
    else gcd(i1, i % i1)
  }
}
