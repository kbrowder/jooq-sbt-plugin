
sbtPlugin := true

version := "2.0-SNAPSHOT"

organization := "me.browder"

name := "jooq-sbt-plugin"

//libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "1.0.2" //for 2.11

crossScalaVersions := Seq(
  "2.10.0", "2.10.1", "2.10.2", "2.10.3", "2.10.4"
//  "2.11.1", "2.11.2" //not yet
).reverse

licenses := Seq("The Apache Software License, Version 2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt"))

homepage := Some(url("http://github.com/kbrowder/jooq-sbt-plugin"))

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

pomExtra := (
  <scm>
    <url>git@github.com:kbrowder/jooq-sbt-plugin.git</url>
    <connection>scm:git:git@github.com:kbrowder/jooq-sbt-plugin.git</connection>
  </scm>
  <developers>
    <developer>
      <id>kbrowder</id>
      <name>Kevin Browder</name>
    </developer>
    <developer>
      <id>sean8223</id>
      <name>Sean Wellington</name>
    </developer>
  </developers>)
