import sbt._
import sbt.Keys.{libraryDependencies, _}

lazy val cop = (project in file("cop"))
  .settings(commonSettings)
  .settings(
    name := "cop",
    exportJars := true
  )

lazy val server = (project in file("socket"))
  .settings(commonSettings)
  .settings(
    name := "socket"
  )
  /*.aggregate(common).dependsOn(common)*/

lazy val sip = (project in file("sip"))
  .settings(commonSettings)
  .settings(
    name := "sip"
  )

lazy val dashboard = (project in file("dashboard"))
  .enablePlugins(PlayScala)
  .settings(commonSettings)
  .settings(
    name := "dashboard",
    libraryDependencies ++=Seq(
      filters,
      guice,
      jdbc,
      evolutions,
      "org.scalikejdbc"        %% "scalikejdbc"                  % scalikejdbcVersion,
      "org.scalikejdbc"        %% "scalikejdbc-config"           % scalikejdbcVersion,
      "org.scalikejdbc"        %% "scalikejdbc-jsr310"           % scalikejdbcVersion,
      "org.scalikejdbc"        %% "scalikejdbc-play-initializer" % "2.7.1-scalikejdbc-3.3",
      "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % Test,
      "com.h2database"         %  "h2"                           % "1.4.192",
    )
  )

lazy val grammar = (project in file("grammar"))
  .settings(commonSettings)
  .settings(
    name := "grammar",
    libraryDependencies ++=Seq(
    )
  )

lazy val commonSettings = Seq(
  scalaVersion := "2.12.8",
  version := "0.1-SNAPSHOT",
  scalacOptions ++= Seq("-unchecked", "-feature", "-language:implicitConversions", "-language:higherKinds", "-language:postfixOps"),
  libraryDependencies ++= Seq(
    // test
    "org.scalatest" %% "scalatest" % "3.0.5" % "test",
    "org.wvlet.airframe" %% "airspec" % airSpecVersion % "test",

    // akka
    "com.typesafe.akka" %% "akka-actor" % "2.6.0-M5",
    "com.typesafe.akka" %% "akka-testkit" % "2.6.0-M5" % Test,
  ),
  testFrameworks += new TestFramework("wvlet.airspec.Framework")
)

name := "training"

version := "0.1"

scalaVersion := "2.12.8"

val airSpecVersion = "19.8.8"

val scalikejdbcVersion = "2.5.2"
