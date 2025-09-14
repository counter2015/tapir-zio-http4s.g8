import sbt._

object Dependencies {

  val tapirVersion = "1.11.44"

  val tapir = Seq(
    "com.softwaremill.sttp.tapir" %% "tapir-http4s-server-zio" % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion,
    "com.softwaremill.sttp.tapir" %% "tapir-redoc-bundle" % tapirVersion,
    "org.http4s" %% "http4s-blaze-server" % "0.23.17"
  )

  val zioLoggingVersion = "2.5.1"
  val log =  Seq(
    "ch.qos.logback" % "logback-classic" % "1.5.18",
    "dev.zio" %% "zio-logging" % zioLoggingVersion,
    "dev.zio" %% "zio-logging-slf4j" % zioLoggingVersion,
  )

  val coreDependency = tapir ++ log
}
