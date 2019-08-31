package jp.co.who.sample

import org.scalajs.dom
import dom.document

object TutorialApp {
  def main(args: Array[String]): Unit = {
    //println("Hello world!")
    appendPar(document.body, "Hello World")
  }

  def appendPar(targetNode: dom.Node, text: String): Unit = {
    val parNode = document.createElement("p")
    val textNode = document.createTextNode(text)
    parNode.appendChild(textNode)
    targetNode.appendChild(parNode)
  }
}
