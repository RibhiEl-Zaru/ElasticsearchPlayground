name := "ElasticSearchPlayground"

version := "0.1"

scalaVersion := "2.12.11"

val circeVersion = "0.12.3"
val circeExtrasVersion = "0.12.1"
val AkkaVersion = "2.5.32"
val alpakkaVersion = "2.0.1"
val scalaTestVersion = "3.2.7"
val elastic4sVersion = "7.12.0"
val AkkaHttpVersion = "10.2.6"
val finchVersion = "0.31.0"


libraryDependencies ++= Seq(
  "com.lightbend.akka" %% "akka-stream-alpakka-elasticsearch" % alpakkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-generic-extras" % circeExtrasVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "org.scalactic" %% "scalactic" % scalaTestVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "de.heikoseeberger" %% "akka-http-circe" % "1.37.0",
  "com.sksamuel.elastic4s" %% "elastic4s-client-esjava" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-json-circe" % elastic4sVersion,
  // test kit
  "com.sksamuel.elastic4s" %% "elastic4s-testkit" % elastic4sVersion % "test"


)

