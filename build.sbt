name := "SocMint Project"

version := "1.0"

scalaVersion := "2.12.15"
assemblyMergeStrategy in assembly := {
 case PathList("META-INF", _*) => MergeStrategy.discard
 case _                        => MergeStrategy.first
}
libraryDependencies += "org.apache.spark" % "spark-streaming_2.12" % "3.3.1"
// libraryDependencies += "org.apache.spark" %% "spark-streaming" % "3.3.1" % "provided"
libraryDependencies += "org.apache.spark" % "spark-streaming-kafka-0-10_2.12" % "3.3.1"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.3.1"
libraryDependencies += "org.apache.hudi" %% "hudi-spark3.3-bundle" % "0.12.1"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.2"
libraryDependencies += "com.bazaarvoice.jolt" % "jolt-core" % "0.0.16"
libraryDependencies += "com.bazaarvoice.jolt" % "json-utils" % "0.0.16"
libraryDependencies += "org.mongodb.spark" % "mongo-spark-connector_2.12" % "10.1.1"
libraryDependencies += "com.typesafe" % "config" % "1.4.2"

// assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
// assembly / assemblyJarName := "something.jar"
