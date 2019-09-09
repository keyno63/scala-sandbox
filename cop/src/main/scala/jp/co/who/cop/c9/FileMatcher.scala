package jp.co.who.cop.c9

import java.io.File

object FileMatcher {
  private def fileHere: Array[File]  = new java.io.File(".").listFiles
  def fileMatching(matcher: String => Boolean): Array[File] = {
    fileHere.filter(file => matcher(file.getName))
  }
  def fileEnding(query: String): Array[File] =
    for (file <- fileHere; if file.getName.endsWith(query))
      yield file
  def fileEnding1(query: String): Array[File] =
    fileMatching(_.endsWith(query))

  def fileContaining(query: String): Array[File] =
    fileMatching(_.contains(query))

  def fileRegex(query: String): Array[File] =
    fileMatching(_.matches(query))

}
