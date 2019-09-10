package jp.co.who.cop.c9

object Sample extends App {

  FileMatcher.fileRegex(".*").foreach(println(_))

}
