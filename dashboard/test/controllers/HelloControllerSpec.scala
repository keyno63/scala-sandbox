package controllers

import org.scalatestplus.play.PlaySpec
import play.api.test.FakeRequest
import play.api.test.Helpers._
import wvlet.airspec._

class HelloControllerSpec extends PlaySpec with AirSpec {

  def controller = new HelloController(stubControllerComponents())

  "get" should {
    "クエリーパラメータがある場合は「Hello, namae!」というレスポンスを返す" in {
      val name = "namae"
      val result = controller.get(Some(name))(FakeRequest())

      status(result) shouldBe 200
      contentAsString(result) shouldBe s"Hello, $name!"
    }

    """クエリーパラメータがない場合は「Please give a name as a query parameter named "name".」というレスポンスを返す""" in {
      val result = controller.get(None)(FakeRequest())

      status(result) shouldBe 200
      contentAsString(result) shouldBe """Please give a name as a query parameter named "name"."""
    }
  }

  "plus" should {
    "クエリーパラメータが指定されている場合は合計値のレスポンスを返す" in {
      val seq = Seq("1", "2")
      val result = controller.get(Some(seq.head))(FakeRequest())

      status(result) shouldBe 200
      contentAsString(result) shouldBe seq.map(_.toInt).sum
    }

    """クエリーパラメータがない場合は「Please give a name as a query parameter named "name".」というレスポンスを返す""" in {
      val result = controller.get(None)(FakeRequest())

      status(result) shouldBe 200
      contentAsString(result) shouldBe """Please give a name as a query parameter named "name"."""
    }
  }
}