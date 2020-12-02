import sbt._
import sbt.Keys.{ skip, _ }

lazy val cop = (project in file("draft/cop"))
  .settings(commonSettings)
  .settings(airSpecSettings)
  .settings(
    name := "cop",
    exportJars := true
  )

lazy val gof = (project in file("draft/gof"))
  .settings(commonSettings)
  .settings(airSpecSettings)
  .settings(
    name := "gof",
    exportJars := true
  )

lazy val socket = (project in file("prototype/socket"))
  .settings(commonSettings)
  .settings(
    name := "socket"
  )
/*.aggregate(common).dependsOn(common)*/

lazy val sip = (project in file("prototype/sip"))
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
    libraryDependencies ++= Seq(
      filters,
      guice,
      jdbc,
      evolutions,
      "org.scalikejdbc"        %% "scalikejdbc"                  % Version.scalikejdbcVersion,
      "org.scalikejdbc"        %% "scalikejdbc-config"           % Version.scalikejdbcVersion,
      "org.scalikejdbc"        %% "scalikejdbc-play-initializer" % Version.scalikejdbcInitializerVersion,
      "org.scalatestplus.play" %% "scalatestplus-play"           % "5.1.0" % Test,
      "com.h2database"         % "h2"                            % "1.4.200",
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
    ).map(_ % Version.circeVersion)
  )

import Dependency._
lazy val grammar = (project in file("draft/grammar"))
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
      //macroParadise,
      //kindProjector,
      resetAllAttrs
    ),
    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding",
      "UTF-8",
      //"-Ypartial-unification",
      "-feature",
      "-language:_"
    )
  )
  .settings(
    name := "grammar",
    libraryDependencies ++= Seq(
      )
  )
lazy val awsclient = (project in file("tools/awsclient"))
  .settings(commonSettings)
  .settings(
    name := "awsclient",
    libraryDependencies ++= Seq(
      "com.amazonaws" % "aws-java-sdk-s3" % Version.AwsSdkVersion
    )
  )

lazy val gcpclient = (project in file("tools/gcpclient"))
  .settings(commonSettings)
  .settings(
    name := "gcpclient",
    libraryDependencies ++= Seq(
      "com.google.cloud" % "google-cloud-storage" % Version.googleCloudVersion
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

lazy val amm = (project in file("tools/amm"))
  .settings(commonSettings)
  .settings(
    name := "amm",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_ % Version.circeVersion),
    libraryDependencies ++= Seq(
      "org.skinny-framework" %% "skinny-http-client" % "3.1.0",
      "log4j"                % "log4j"               % "1.2.17",
      "org.slf4j"            % "slf4j-log4j12"       % "1.7.26" % Test
    )
  )

// fp libs
lazy val zio = (project in file("functional/zio"))
  .settings(commonSettings)
  .settings(
    name := "zio",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio"      % Version.zioVersion,
      "dev.zio" %% "zio-test" % Version.zioVersion % "test"
    ) ++ Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_               % Version.circeVersion) ++
      Seq("org.scalatest" %% "scalatest" % "3.2.0" % "test") // unique defined
  )

// http libs
lazy val `akka-sample` = (project in file("http/akka-sample"))
  .settings(commonSettings)
  .settings(
    name := "akka-sample",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"   % Version.akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream" % Version.akkaVersion
    )
  )

lazy val `http4s-sample` = (project in file("http/http4s-sample"))
  .settings(commonSettings)
  .settings(
    name := "http4s-sample",
    libraryDependencies ++= Seq(
      "org.http4s" %% "http4s-server"       % Version.http4sVersion,
      "org.http4s" %% "http4s-blaze-server" % Version.http4sVersion,
      "org.http4s" %% "http4s-dsl"          % Version.http4sVersion,
      // log
      "org.log4s" %% "log4s"        % Version.log4sVersion,
      "org.slf4j" % "slf4j-log4j12" % Version.log4j12
    )
  )

lazy val `skinny-sample` = (project in file("http/skinny-sample"))
  .settings(
    name := "skinny-sample",
    libraryDependencies ++= Seq(
      "org.skinny-framework" %% "skinny-http-client"
    ).map(_ % Version.skinnyVersion) ++ Seq(
      // log
      "org.slf4j" % "slf4j-log4j12" % Version.log4j12 % Test,
      "log4j"     % "log4j"         % "1.2.17"
    ) ++ Seq(
      // config
      "com.typesafe"          % "config"      % "1.4.1",
      "com.github.pureconfig" %% "pureconfig" % "0.14.0"
    )
  )

lazy val `finch-sample` = (project in file("http/finch-sample"))
  .settings(commonSettings)
  .settings(
    name := "finch-sample",
    libraryDependencies ++= Seq(
      "com.github.finagle" %% "finchx-core",
      "com.github.finagle" %% "finchx-circe"
    ).map(_               % Version.finchVersion) ++
      Seq("org.scalatest" %% "scalatest" % "3.2.0" % "test") // unique defined
  )

// json
lazy val `circe-sample` = (project in file("json/circe-sample"))
//.settings(commonSettings)
  .settings(
    name := "circe-sample",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser"
    ).map(_               % Version.circeVersion) ++
      Seq("org.scalatest" %% "scalatest" % "3.2.0" % "test") // unique defined
  )

// db libs
lazy val `scalikejdbc-sample` = (project in file("db/scalikejdbc-sample"))
  .settings(commonSettings)
  .settings(
    name := "scalikejdbc-sample",
    libraryDependencies ++= Seq(
      "org.scalikejdbc" %% "scalikejdbc"      % Version.scalikejdbcVersion,
      "org.scalikejdbc" %% "scalikejdbc-test" % Version.scalikejdbcVersion % Test, // for test
      "com.h2database"  % "h2"                % Version.h2Version,
      "ch.qos.logback"  % "logback-classic"   % "1.2.+"
    )
  )

lazy val `doobie-sample` = (project in file("db/doobie-sample"))
  .settings(commonSettings)
  .settings(
    name := "doobie-sample",
    libraryDependencies ++= Seq(
      "org.tpolecat" %% "doobie-core" % Version.doobieVersion,
      "org.tpolecat" %% "doobie-h2"   % Version.doobieVersion
    )
  )

// settings
lazy val commonSettings = Seq(
  scalaVersion := Version.scalaBaseVersion,
  version := "0.1-SNAPSHOT",
  libraryDependencies ++= Seq(
    // test
    "org.scalatest" %% "scalatest" % Version.scalatestVersion % "test",
    // akka
    "com.typesafe.akka" %% "akka-actor"   % Version.akkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % Version.akkaVersion % Test
  )
)

lazy val airSpecSettings = Seq(
  scalacOptions ++= Seq(
    "-unchecked",
    "-feature",
    "-language:implicitConversions",
    "-language:higherKinds",
    "-language:postfixOps"
  ),
  libraryDependencies += "org.wvlet.airframe" %% "airspec" % Version.airSpecVersion % "test",
  testFrameworks += new TestFramework("wvlet.airspec.Framework")
)

lazy val scalaJsSettings = Seq(
  scalaVersion := Version.scalaBaseVersion,
  version := "0.1-SNAPSHOT",
  scalacOptions += "-P:scalajs:sjsDefinedByDefault",
  //scalaJSLinkerConfig ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
  scalaJSUseMainModuleInitializer := true,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom"   % "1.0.0",
    "org.querki"   %%% "jquery-facade" % "1.2"
  ),
  skip in packageJSDependencies := false,
  jsDependencies += "org.webjars" % "jquery" % "2.2.1" / "jquery.js" minified "jquery.min.js"
)

lazy val scalaJsSpecSettings = Seq(
  scalacOptions ++= Seq(
    "-unchecked",
    "-feature",
    "-language:implicitConversions",
    "-language:higherKinds",
    "-language:postfixOps"
  ),
  libraryDependencies += "org.wvlet.airframe" %%% "airspec" % Version.airSpecVersion % "test",
  testFrameworks += new TestFramework("wvlet.airspec.Framework")
)

name := "scala-sandbox"

version := "0.1"

scalaVersion := Version.scalaBaseVersion

// command alias.
addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias(
  "check",
  "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck"
)
