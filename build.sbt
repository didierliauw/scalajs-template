import org.scalajs.sbtplugin.cross.CrossProject

enablePlugins(ScalaJSPlugin)

lazy val scalaV = "2.12.2"
lazy val akkaHttpVersion = "10.0.6"
lazy val upickleV = "0.4.4"
lazy val akkaJsVersion = "1.2.5.2"

lazy val scalaJsProject =
  (crossProject in file ("."))
    .settings(
      scalaVersion := scalaV,
      libraryDependencies ++= Seq(
        "com.lihaoyi" %%% "upickle" % upickleV,
        "com.lihaoyi" %%% "utest" % "0.4.4" % "test",
        "com.lihaoyi" %%% "scalatags" % "0.6.5"
      )
    ).jsSettings(
      scalaJSUseMainModuleInitializer := true,
      testFrameworks += new TestFramework("utest.runner.Framework"),
      libraryDependencies ++= Seq(
        "org.akka-js" %%% "akkajsactor" % akkaJsVersion,
        "org.akka-js" %%% "akkajsactorstream" % akkaJsVersion,
        "org.scala-js" %%% "scalajs-dom" % "0.9.1"
      )
    ).jvmSettings(
      libraryDependencies ++= Seq(
        "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
        "com.typesafe.akka" %% "akka-http" % akkaHttpVersion
      )
    )

lazy val jvm = scalaJsProject.jvm
lazy val js = scalaJsProject.js

