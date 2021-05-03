name := "ElasticSearchPlayground"

version := "0.1"

scalaVersion := "2.13.5"

val circeVersion = "0.12.3"
val circeExtrasVersion = "0.12.1"
val AkkaVersion = "2.5.31"
val alpakkaVersion = "2.0.1"


libraryDependencies ++= Seq(
  "com.lightbend.akka" %% "akka-stream-alpakka-elasticsearch" % alpakkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-generic-extras" % circeExtrasVersion,
  "io.circe" %% "circe-parser" % circeVersion
)
