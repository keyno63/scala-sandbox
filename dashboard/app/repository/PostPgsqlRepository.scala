package repository
import form.Post
import scalikejdbc._
import scalikejdbc.jsr310._

object PostPgsqlRepository {
  // データベースに接続
  val url = "jdbc:postgresql://localhost:5432/postgres"
  val user = "postgres"
  val password = "postgres"
  ConnectionPool.singleton(url, user, password)

  def findAll: Seq[Post] = DB("pgsql") readOnly { implicit session =>
    sql"SELECT name, date FROM company".map { rs =>
      Post(rs.long("id"), rs.string("body"), rs.offsetDateTime("date"))
    }.list().apply()
  }

  def add(post: Post): Unit = DB("pgsql") localTx { implicit session =>
    sql"INSERT INTO posts (body, date) VALUES (${post.body}, ${post.date})".update().apply()
  }
}