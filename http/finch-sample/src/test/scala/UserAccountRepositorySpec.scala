import org.scalatest.flatspec._

class UserAccountRepositorySpec extends AnyFlatSpec {

  "id is 0" should "Alice Account" in {
    assert(
      UserAccountRepository.find("0") == Some(UserAccount("0", "Alice"))
    )
    assert(
      UserAccountRepository.find("1") == Some(UserAccount("1", "Bob"))
    )
    assert(
      UserAccountRepository.find("2") == Some(UserAccount("2", "Crith"))
    )
  }

}
