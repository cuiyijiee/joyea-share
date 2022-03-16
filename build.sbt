organization := "com.cuje"
name := "joyea_share"
version := "1.0-SNAPSHOT"

scalaVersion := "2.13.8"
scalacOptions ++= Seq("-deprecation", "-feature", "-unchecked")

// Xitrum requires Java 8
javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

//------------------------------------------------------------------------------

libraryDependencies += "tv.cntt" %% "xitrum" % "3.30.1"

libraryDependencies += "org.json4s" %% "json4s-ext" % "3.6.10"

// Xitrum uses SLF4J, an implementation of SLF4J is needed
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"

// For writing condition in logback.xml
libraryDependencies += "org.codehaus.janino" % "janino" % "3.1.2"
libraryDependencies += "commons-net" % "commons-net" % "3.6"
libraryDependencies += "commons-io" % "commons-io" % "2.7"
// https://mvnrepository.com/artifact/com.github.etaty/rediscala
libraryDependencies += "com.github.etaty" %% "rediscala" % "1.9.0"

// https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
libraryDependencies += "com.squareup.okhttp3" % "okhttp" % "4.9.0"

libraryDependencies ++= Seq(
  "org.scalikejdbc" %% "scalikejdbc-async" % "0.14.0",
  "org.scalikejdbc" %% "scalikejdbc-syntax-support-macro" % "3.5.0",
  "com.github.jasync-sql" % "jasync-mysql" % "1.1.3",
  "mysql" % "mysql-connector-java" % "8.0.19"
)

// https://mvnrepository.com/artifact/com.alibaba/easyexcel
libraryDependencies += "com.alibaba" % "easyexcel" % "2.2.6"

// Put config directory in classpath for easier development --------------------
// For "sbt console"
Compile / unmanagedClasspath += baseDirectory.value / "config"
// For "sbt fgRun"
Runtime / unmanagedClasspath += baseDirectory.value / "config"

// Copy these to target/xitrum when sbt xitrum-package is run
XitrumPackage.copy("config", "public", "script")
