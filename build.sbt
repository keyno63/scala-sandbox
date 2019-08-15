import sbt.Keys._

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

lazy val commonSettings = Seq(
  scalaVersion := "2.12.8",
  version := "0.1-SNAPSHOT",
  scalacOptions ++= Seq("-unchecked", "-feature", "-language:implicitConversions", "-language:higherKinds", "-language:postfixOps"),
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.5" % "test",

    // akka
    "com.typesafe.akka" %% "akka-actor" % "2.6.0-M5",
    "com.typesafe.akka" %% "akka-testkit" % "2.6.0-M5" % Test,
  )
)

name := "training"

version := "0.1"

scalaVersion := "2.12.8"

