import org.scalatest.flatspec.AnyFlatSpec

class BigDecimalTest extends AnyFlatSpec {

  /**
   * pattern henkan
   * - Int
   * - Long
   * - BigInt
   * - Float
   * - Double
   * - BigDecimal
   * // seisuu
   * int < long < big int
   * // syousuu
   * float < double < big decimal
   * https://github.com/scala/scala/blob/8a2cf63ee5bad8c8c054f76464de0e10226516a0/src/library/scala/math/BigDecimal.scala
   */
  "big decimal" should "convert" in {
    // int
    val value1 = BigDecimal("1")
    conv(value1)

    // long
    val _long: Long = 10000000000L
    conv(_long)

    // float
    val _float = BigDecimal.decimal(Float.MaxValue) + 1 - 1
    conv(_float)

    // double
    val _double0 = BigDecimal.decimal(Float.MaxValue) + 1
    conv(_double0)
    val _double = BigDecimal.decimal(Double.MaxValue) + 1 - 1
    conv(_double)

    //val _bigint = Double.MaxValue + 1
    val _bigint = BigDecimal(Double.MaxValue) + 1
    conv(_bigint)
//    conv("0.1")
//    conv("1000000000000000000000000000000000000000000000000.1")
//    conv("1000000000000000000000")
//    conv("1" + "0" * 35)
    conv("1" + "0" * 13 + "0.1")
    conv("1" + "0" * 35 + "0.1")
  }

  "big decimal 2" should "convert" in {
    for (x <- Range(0, 20)) {
      conv("1" + "0" * x)
    }
  }

  def conv(v: String): Unit = {
    val value = BigDecimal(v)
    conv(value)
  }

  def conv(v: BigDecimal): Unit = {
    println(s"value is ${v.toString}")
    // int
    print("isValidInt", v.isValidInt.toString)
    // long
    print("isValidLong", v.isValidLong.toString)
    // float
    print("isDecimalFloat", v.isDecimalFloat.toString)
    print("isBinaryFloat", v.isBinaryFloat.toString)
    print("isExactFloat", v.isExactFloat.toString)
    // double
    print("isDecimalDouble", v.isDecimalDouble.toString)
    print("isBinaryDouble", v.isBinaryDouble.toString)
    print("isExactDouble", v.isExactDouble.toString)

    print("isWhole", v.isWhole.toString)

    println(s"===== converted = [${conversionValue(v).getClass.toString}]")
  }

  def print(funcName: String, s: String) = println(s"$funcName is [$s]")

  def conversionValue(v: BigDecimal) =
    if (v.isWhole) {
      if (v.isValidInt) v.toInt
      else if (v.isValidLong) v.toLong
      else if (v.isDecimalFloat) v.toFloat
      else if (v.isDecimalDouble) v.toDouble
      else v.toBigInt()
    } else {
      if (v.isDecimalFloat) v.toFloat
      else if (v.isDecimalDouble) v.toDouble
      else v
    }
}
