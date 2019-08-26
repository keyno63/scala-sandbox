package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneServerPerTest
import wvlet.airspec.AirSpec

class TextboardSpec
  extends PlaySpec with AirSpec with GuiceOneServerPerTest
    with OneBrowserPerSuite with HtmlUnitFactory {

  import org.openqa.selenium.htmlunit.HtmlUnitDriver

  override def createWebDriver() = {
    val driver = new HtmlUnitDriver {
      def setAcceptLanguage(lang: String) =
        this.getWebClient().addRequestHeader("Accept-Language", lang)
    }
    driver.setAcceptLanguage("en")
    driver
  }

  def `GETリクエスト 何も投稿しない場合はメッセージを表示しない`(): Unit = {
    go to s"http://localhost:$port/"
    pageTitle shouldBe "Scala Text Textboard"
    findAll(className("post-body")).length shouldBe 0
  }

  def `POSTリクエスト 投稿したものが表示される`(): Unit = {
    val body = "test post"

    go to s"http://localhost:$port/"
    textField(cssSelector("input#post")).value = body
    submit()

    eventually {
      val posts = findAll(className("post-body")).toSeq
      posts.length shouldBe 1
      posts.head.text shouldBe body
      findAll(cssSelector("p#error")).length shouldBe 0
    }
  }

  def `POST 投稿したものが表示される その2`(): Unit = {
    go to s"http://localhost:$port/"

    eventually {
      val posts = findAll(className("post-body")).toSeq
      posts.length shouldBe 1
    }
  }


  def `空のメッセージは投稿できない`(): Unit = {
    val body = ""

    go to s"http://localhost:$port/"
    textField(cssSelector("input#post")).value = body
    submit()

    eventually {
      val error = findAll(cssSelector("p#error")).toSeq
      error.length shouldBe  1
      error.head.text shouldBe "Please enter a message."
    }
  }

  def `長すぎるメッセージは投稿できない`(): Unit = {
    val body = "too long messages"

    go to s"http://localhost:$port/"
    textField(cssSelector("input#post")).value = body
    submit()

    eventually {
      val error = findAll(cssSelector("p#error")).toSeq
      error.length shouldBe 1
      error.head.text shouldBe "The message is too long."
    }
  }
}
