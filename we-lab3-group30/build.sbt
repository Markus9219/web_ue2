name := """we-lab3-group30"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "com.google.code.gson" % "gson" % "2.2"
)

TwirlKeys.templateImports += "scala.collection._"
TwirlKeys.templateImports += "at.ac.tuwien.big.we15.lab2.api._"
