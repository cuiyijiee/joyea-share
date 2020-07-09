organization := "com.cuje"
name := "joyea_share"
version := "1.0-SNAPSHOT"

scalaVersion := "2.12.10"
scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked")

// Xitrum requires Java 8
javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

//------------------------------------------------------------------------------

libraryDependencies += "tv.cntt" %% "xitrum" % "3.29.0"

// Xitrum uses SLF4J, an implementation of SLF4J is needed
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"

// For writing condition in logback.xml
libraryDependencies += "org.codehaus.janino" % "janino" % "3.0.11"
libraryDependencies += "commons-net" % "commons-net" % "3.6"
libraryDependencies += "commons-io" % "commons-io" % "2.7"
// https://mvnrepository.com/artifact/com.github.etaty/rediscala
libraryDependencies += "com.github.etaty" %% "rediscala" % "1.9.0"

// https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
libraryDependencies += "com.squareup.okhttp3" % "okhttp" % "3.14.1"

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc" % "3.4.2",
  "org.scalikejdbc" %% "scalikejdbc-async" % "0.13.0",
  "org.scalikejdbc" %% "scalikejdbc-syntax-support-macro" % "3.4.2",
  "com.github.jasync-sql" % "jasync-mysql" % "1.1.1-kts-9",
  "mysql" % "mysql-connector-java" % "8.0.19"
)

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-blaze-client" % "0.21.4",
)

// https://mvnrepository.com/artifact/com.alibaba/easyexcel
libraryDependencies += "com.alibaba" % "easyexcel" % "2.2.3"

// Put config directory in classpath for easier development --------------------
// For "sbt console"
unmanagedClasspath in Compile += baseDirectory.value / "config"

// For "sbt fgRun"
unmanagedClasspath in Runtime += baseDirectory.value / "config"

// Copy these to target/xitrum when sbt xitrum-package is run
XitrumPackage.copy("config", "public", "script")
