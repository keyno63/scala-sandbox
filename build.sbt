import sbt._
import sbt.Keys.{skip, _}

lazy val cop = (project in file("cop"))
  .settings(commonSettings)
  .settings(airSpecSettings)
  .settings(
    name := "cop",
    exportJars := true
  )

lazy val gof = (project in file("gof"))
  .settings(commonSettings)
  .settings(airSpecSettings)
  .settings(
    name := "gof",
    exportJars := true
  )

lazy val socket = (project in file("socket"))
  .settings(commonSettings)
  .settings(
    name := "socket"
  )
  /*.aggregate(common).dependsOn(common)*/

lazy val sip = (project in file("sip"))
  .settings(commonSettings)
  .settings(airSpecSettings)
  .settings(
    name := "sip"
  )

lazy val dashboard = (project in file("dashboard"))
  .enablePlugins(PlayScala)
  .settings(commonSettings)
  .settings(airSpecSettings)
  .settings(
    name := "dashboard",
    fork in run := true,
    libraryDependencies ++=Seq(
      filters,
      guice,
      jdbc,
      evolutions,
      "org.scalikejdbc"        %% "scalikejdbc"                  % scalikejdbcVersion,
      "org.scalikejdbc"        %% "scalikejdbc-config"           % scalikejdbcVersion,
      "org.scalikejdbc"        %% "scalikejdbc-play-initializer" % scalikejdbcInitializerVersion,
      "org.scalatestplus.play" %% "scalatestplus-play"           % "5.1.0" % Test,
      "com.h2database"         %  "h2"                           % "1.4.200",
      "com.dripower"           %% "play-circe"                   % "2812.0",
      // https://mvnrepository.com/artifact/org.postgresql/postgresql
      "org.postgresql" % "postgresql" % "42.2.14",
      // https://mvnrepository.com/artifact/mysql/mysql-connector-java
      "mysql" % "mysql-connector-java" % "8.0.21"
    ),
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_ % circeVersion)
  )

import Dependency._
lazy val grammar = (project in file("grammar"))
  .settings(commonSettings)
  .settings(airSpecSettings)
  .settings(
    // cats settings.
    libraryDependencies ++= Seq(
      catsCore,
      catsFree,
      catsMtl,
      simulacrum,
      //specs2Core % Test, specs2Scalacheck % Test, scalacheck % Test,
      macroParadise, kindProjector, resetAllAttrs
    ),
    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding", "UTF-8",
      "-Ypartial-unification",
      "-feature",
      "-language:_"
    )
  )
  .settings(
    name := "grammar",
    libraryDependencies ++=Seq(
    )
  )

lazy val gcpclient = (project in file("gcpclient"))
  .settings(commonSettings)
  .settings(
    name := "gcpclient",
    libraryDependencies ++=Seq(
      "com.google.cloud" % "google-cloud-storage" % "1.87.0"
    )
  )

//lazy val scalajs = (project in file("scalajs"))
//  .enablePlugins(ScalaJSPlugin)
//  .settings(scalaJsSpecSettings)
//  .settings(scalaJsSettings)
//  .settings(
//    name := "scalajs",
//    artifactPath in (Compile, fastOptJS) := baseDirectory.value / "dist" / "scalajs.js",
//  )

lazy val amm = (project in file("amm"))
  .settings(commonSettings)
  .settings(
    name := "amm",
    libraryDependencies ++= Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
    ).map(_ % circeVersion),
    libraryDependencies ++= Seq(
      "org.skinny-framework" %% "skinny-http-client" % "3.0.1",
      "log4j" % "log4j" % "1.2.17",
      "org.slf4j" % "slf4j-log4j12" % "1.7.26" % Test,
    )
  )

lazy val zio = (project in file("zio"))
  .settings(commonSettings)
  .settings(
    name := "zio",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % zioVersion
    )
  )

lazy val `akka-sample` = (project in file("akka-sample"))
  .settings(commonSettings)
  .settings(
    name := "akka-sample",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"           % "10.1.12",
      "com.typesafe.akka" %% "akka-stream"         % akkaVersion
    )
  )

lazy val `htt4s-sample` = (project in file("htt4s-sample"))
  .settings(commonSettings)
  .settings(
    name := "htt4s-sample",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-server"       % htt4sVersion,
      "org.http4s" %% "http4s-blaze-server" % htt4sVersion,
      "org.http4s" %% "http4s-dsl"          % htt4sVersion,
      // log
      "org.log4s" %% "log4s"                % log4sVersion,
      "org.slf4j"  % "slf4j-log4j12"        % log4j12,
    )
  )

// settings
lazy val commonSettings = Seq(
  scalaVersion := scalaBaseVersion,
  version := "0.1-SNAPSHOT",
  libraryDependencies ++= Seq(
    // test
    "org.scalatest" %% "scalatest" % "3.0.5" % "test",

    // akka
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  ),
)

lazy val airSpecSettings = Seq(
  scalacOptions ++= Seq("-unchecked", "-feature", "-language:implicitConversions", "-language:higherKinds", "-language:postfixOps"),
  libraryDependencies += "org.wvlet.airframe" %% "airspec" % airSpecVersion % "test",
  testFrameworks += new TestFramework("wvlet.airspec.Framework"),
)

lazy val scalaJsSettings = Seq(
  scalaVersion := scalaBaseVersion,
  version := "0.1-SNAPSHOT",
  scalacOptions += "-P:scalajs:sjsDefinedByDefault",
  //scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
  scalaJSUseMainModuleInitializer := true,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "1.0.0",
    "org.querki" %%% "jquery-facade" % "1.2",
  ),
  skip in packageJSDependencies := false,
  jsDependencies += "org.webjars" % "jquery" % "2.2.1" / "jquery.js" minified "jquery.min.js"
)

lazy val scalaJsSpecSettings = Seq(
  scalacOptions ++= Seq("-unchecked", "-feature", "-language:implicitConversions", "-language:higherKinds", "-language:postfixOps"),
  libraryDependencies += "org.wvlet.airframe" %%% "airspec" % airSpecVersion % "test",
  testFrameworks += new TestFramework("wvlet.airspec.Framework"),
)

name := "training"

version := "0.1"

scalaVersion := scalaBaseVersion

val airSpecVersion = "19.8.8"

val scalikejdbcVersion = "3.4.0"

val scalaBaseVersion = "2.12.12"

val scalikejdbcInitializerVersion = "2.8.0-scalikejdbc-3.4"

val zioVersion = "1.0.0-RC21"

lazy val circeVersion = "0.13.0"

lazy val akkaVersion = "2.6.5"

lazy val htt4sVersion = "0.21.6"

lazy val log4sVersion = "1.8.2"

lazy val log4j12 = "1.7.30"
