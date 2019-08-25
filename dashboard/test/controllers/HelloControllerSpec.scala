package controllers

import play.api.test.FakeRequest
import play.api.test.Helpers._
import wvlet.airspec._

class HelloControllerSpec extends AirSpec {

  val controller = new HelloController(stubControllerComponents())

  def `root_クエリーパラメータがある場合は「Hello, namae!」というレスポンスを返す`(): Unit = {
    val name = "namae"
    val result = controller.get(Some(name))(FakeRequest())

    status(result) shouldBe 200
    contentAsString(result) shouldBe s"Hello, $name!"
  }

  def `root_クエリーパラメータがない場合は「Please give a name as a query parameter named "name".」というレスポンスを返す`():Unit = {
    val result = controller.get(None)(FakeRequest())

    status(result) shouldBe 200
    contentAsString(result) shouldBe """Please give a name as a query parameter named "name"."""
  }

  def `クエリーパラメータが指定されている場合は合計値のレスポンスを返す"`(): Unit = {
    val seq = Seq(1, 2)
    val result = controller.plus(Some(seq.head), Some(seq(1)))(FakeRequest(GET, "/plus"))

    status(result) shouldBe 200
    contentAsString(result) shouldBe seq.sum.toString
  }

  def `クエリーパラメータがない場合は 0 のレスポンスを返す`(): Unit = {
    val result =  controller.plus(None, None)(FakeRequest(GET, "/plus"))

    status(result) shouldBe 200
    contentAsString(result) shouldBe 0.toString
  }
}