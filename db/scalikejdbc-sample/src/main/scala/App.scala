import scalikejdbc._

object App extends scala.App {

  Class.forName("org.h2.Driver")
  ConnectionPool.singleton("jdbc:h2:mem:hello", "user", "pass")

  implicit val session = AutoSession

  // define table
  sql"""
        create table members (
          id serial not null primary key,
          name varchar(64),
          created_at timestamp not null
        )
  """.execute.apply()

  Seq("Alice", "Bob", "Chris") foreach { name =>
    sql"insert into members (name, created_at) values (${name}, current_timestamp)".update.apply()
  }

  val entities: List[Map[String, Any]] = sql"SELECT * FROM members".map(_.toMap).list.apply()

  val members: List[Member] = sql"SELECT * FROM members".map(rs => Member(rs)).list.apply()
  val m                     = Member.syntax("m")
  val name                  = "Alice"
  val alice: Option[Member] = withSQL {
    select.from(Member as m).where.eq(m.name, name)
  }.map(rs => Member(rs)).single.apply()

  //sql"SELECT name, population FROM country"
}

import java.time._
case class Member(id: Long, name: Option[String], createdAt: ZonedDateTime)
object Member extends SQLSyntaxSupport[Member] {
  override val tableName          = "members"
  def apply(rs: WrappedResultSet) = new Member(rs.long("id"), rs.stringOpt("name"), rs.zonedDateTime("created_at"))
}
