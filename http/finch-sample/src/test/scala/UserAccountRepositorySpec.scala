import org.scalatest.flatspec._
import org.scalatest.OptionValues._

class UserAccountRepositorySpec extends AnyFlatSpec {

  "id is 0" should "Alice Account" in {
    assert(
      UserAccountRepository.find("0").value == UserAccount("0", "Alice")
    )
    assert(
      UserAccountRepository.find("1").value == UserAccount("1", "Bob")
    )
    assert(
      UserAccountRepository.find("2").value == UserAccount("2", "Crith")
    )
  }

}
