package repository
import form.Post
import scalikejdbc._
import scalikejdbc.jsr310._

object PostPgsqlRepository {

  def findAll: Seq[Post] = DB("pgsql") readOnly { implicit session =>
    sql"SELECT name, date FROM company".map { rs =>
      Post(rs.long("id"), rs.string("body"), rs.offsetDateTime("date"))
    }.list().apply()
  }

  def add(post: Post): Unit = DB("pgsql") localTx { implicit session =>
    sql"INSERT INTO posts (body, date) VALUES (${post.body}, ${post.date})".update().apply()
  }
}