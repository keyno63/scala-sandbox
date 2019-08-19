package jp.co.who.sip.parser

class Parser {
  def sample(data: Any): Unit = data match {
    case s: String => println(s)
    case _ => println("not")
  }

}

object Parser {
  def main(args: Array[String]): Unit = {
    val c = new Parser
    c.sample("hoge")
    c.sample(1)
  }
}
