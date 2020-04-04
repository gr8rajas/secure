name := "WebLogAnalytics"

version := "0.1"

scalaVersion := "2.12.7"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-sql" % "2.4.5",
  "org.apache.spark" %% "spark-core" % "2.4.5"
)

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.12" % "3.0.5" % "test",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
)
