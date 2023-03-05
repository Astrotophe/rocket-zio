ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.2.0"

ThisBuild / scalacOptions ++=
  Seq(
    "-deprecation",
    "-feature",
    "-language:implicitConversions",
    "-unchecked",
    "-Xfatal-warnings",
    "-Ykind-projector",
    "-Ysafe-init",
  ) ++ Seq("-rewrite", "-indent") ++ Seq("-source", "future")


lazy val root = (project in file("."))
  .settings(
    name := "rocket-zio",
    libraryDependencies ++= Dependencies.projectDependencies
  )

testFrameworks += new TestFramework("zio.test.sbt.ZTestFramework")


