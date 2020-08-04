package controllers

import play.api.test.FakeRequest
import play.api.test.Helpers._
import wvlet.airspec._

class HelloControllerSpec extends AirSpec {

  val controller = new HelloController(stubControllerComponents())

  def `root_クエリーパラメーターがある場合は「Hello, namae!」というレスポンスを返す`(): Unit = {
    val name   = "namae"
    val result = controller.get(Some(name))(FakeRequest())
    println(result)
    println(contentAsString(result))

    status(result) shouldBe 200
    contentAsString(result) shouldBe s"Hello, $name!"
  }

  def `root_クエリーパラメーターがない場合は「Please give a name as a query parameter named "name".」というレスポンスを返す`(): Unit = {
    val result = controller.get(None)(FakeRequest())

    status(result) shouldBe 200
    contentAsString(result) shouldBe """Please give a name as a query parameter named "name"."""
  }

  def `クエリーパラメーターが指定されている場合は合計値のレスポンスを返す"`(): Unit = {
    val seq    = Seq(1, 2)
    val result = controller.plus(Some(seq.head), Some(seq(1)))(FakeRequest(GET, "/plus"))

    status(result) shouldBe 200
    contentAsString(result) shouldBe seq.sum.toString
  }

  def `クエリーパラメーターがない場合は 0 のレスポンスを返す`(): Unit = {
    val result = controller.plus(None, None)(FakeRequest(GET, "/plus"))

    status(result) shouldBe 200
    contentAsString(result) shouldBe 0.toString
  }

  def `クエリーパラメーターが足りない場合はもらった値をレスポンスを返す`(): Unit = {
    val result = controller.plus(None, Some(1))(FakeRequest(GET, "/plus"))

    status(result) shouldBe 200
    contentAsString(result) shouldBe 1.toString
  }

  def `クエリーパラメーターが足りない場合はもらった値をレスポンスを返す, その2`(): Unit = {
    val result = controller.plus(Some(2), None)(FakeRequest(GET, "/plus"))

    status(result) shouldBe 200
    contentAsString(result) shouldBe 2.toString
  }
}
