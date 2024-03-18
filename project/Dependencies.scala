import sbt._

object Dependencies {

  val tapirVersion = "1.7.6"

  val tapir = Seq(
    "com.softwaremill.sttp.tapir" %% "tapir-http4s-server-zio" % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-redoc-bundle" % tapirVersion,
    "org.http4s" %% "http4s-blaze-server" % "0.23.16"
  )

  val zioLoggingVersion = "2.2.2"
  val log =  Seq(
    "ch.qos.logback" % "logback-classic" % "1.4.14",
    "dev.zio" %% "zio-logging" % zioLoggingVersion,
    "dev.zio" %% "zio-logging-slf4j" % zioLoggingVersion,
  )

  val coreDependency = tapir ++ log
}
