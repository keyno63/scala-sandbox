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
      guice, 
      "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % Test,
      jdbc,
      evolutions,
      "org.scalikejdbc"        %% "scalikejdbc"                  % scalikejdbcVersion,
      "org.scalikejdbc"        %% "scalikejdbc-config"           % scalikejdbcVersion,
      "org.scalikejdbc"        %% "scalikejdbc-jsr310"           % scalikejdbcVersion,
      "org.scalikejdbc"        %% "scalikejdbc-play-initializer" % "2.5.3",
    )
  )

lazy val commonSettings = Seq(
  scalaVersion := "2.12.8",
  version := "0.1-SNAPSHOT",
  scalacOptions ++= Seq("-unchecked", "-feature", "-language:implicitConversions", "-language:higherKinds", "-language:postfixOps"),
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.5" % "test",

    // akka
    "com.typesafe.akka" %% "akka-actor" % "2.6.0-M5",
    "com.typesafe.akka" %% "akka-testkit" % "2.6.0-M5" % Test,
    "org.wvlet.airframe" %% "airspec" % airSpecVersion % "test",
  ),
  testFrameworks += new TestFramework("wvlet.airspec.Framework")
)

name := "training"

version := "0.1"

scalaVersion := "2.12.8"

val airSpecVersion = "19.8.8"

val scalikejdbcVersion = "3.3.5"
