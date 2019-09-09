package jp.co.who.cop.c9

import java.io.File

object FileMatcher {
  private def fileHere: Array[File]  = new java.io.File(".").listFiles
  def fileEnding(query: String): Array[File] =
    for (file <- fileHere; if file.getName.endsWith(query))
      yield file
  def fileEnding1(query: String): Array[File] =
    fileHere.filter(_.getName.endsWith(query))

  def fileContaining(query: String): Array[File] =
    fileHere.filter(_.getName.contains(query))

  def fileRegex(query: String): Array[File] =
    fileHere.filter(_.getName.matches(query))

}
