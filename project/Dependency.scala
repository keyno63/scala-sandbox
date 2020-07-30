import sbt.librarymanagement.CrossVersion
import sbt._

object Dependency {
  val catsCore = "org.typelevel" %% "cats-core" % Version.catsVersion
  val catsFree = "org.typelevel" %% "cats-free" % Version.catsVersion
  val catsMtl = "org.typelevel" %% "cats-mtl-core" % Version.catsMtlVersion

  val simulacrum = "com.github.mpilquist" %% "simulacrum" % "0.11.0"
  val macroParadise = compilerPlugin("org.scalamacros" % "paradise" % "2.1.1" cross CrossVersion.full)
  val kindProjector = compilerPlugin("org.spire-math" %% "kind-projector" % "0.9.4")
  val resetAllAttrs = "org.scalamacros" %% "resetallattrs" % "1.0.0"

  /*
  val specs2Version = "3.6" // use the version used by discipline
  val specs2Core  = "org.specs2" %% "specs2-core" % specs2Version
  val specs2Scalacheck = "org.specs2" %% "specs2-scalacheck" % specs2Version
  val scalacheck = "org.scalacheck" %% "scalacheck" % "1.12.4"
   */
}

object Version {
  lazy val scalaBaseVersion = "2.12.12"
  // functional
  lazy val catsVersion = "1.0.1"
  lazy val catsMtlVersion = "0.2.1"
  lazy val zioVersion = "1.0.0-RC21"
  // db
  lazy val scalikejdbcVersion = "3.4.0"
  lazy val scalikejdbcInitializerVersion = "2.8.0-scalikejdbc-3.4"
  // json
  lazy val circeVersion = "0.13.0"
  // http
  lazy val akkaVersion = "2.6.5"
  lazy val akkaHttpVersion = "10.1.12"
  lazy val htt4sVersion = "0.21.6"
  lazy val finchVersion = "0.32.1"
  // log
  lazy val log4sVersion = "1.8.2"
  lazy val log4j12 = "1.7.30"
  // test
  lazy val airSpecVersion = "19.8.8"
}
