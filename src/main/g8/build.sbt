import Dependencies._

ThisBuild / scalaVersion := "2.13.7"
ThisBuild / version := "0.1.0-SNAPSHOT"

// for cats Kleisli
addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.13.2" cross CrossVersion.full)

// Automatically reload the build when source changes are detected
Global / onChangedBuildSource := ReloadOnSourceChanges

scalacOptions ++= Seq(
  "-feature", // Emit warning and location for usages of features that should be imported explicitly.
  "-deprecation", // Emit warning and location for usages of deprecated APIs.
  "-encoding", "utf-8", // Specify character encoding used by source files.
  "-explaintypes", // Explain type errors in more detail.
  "-Ymacro-annotations", // circe @JsonCodec
  "-Vimplicits",
  "-Vtype-diffs",
  "-Wunused",
)


lazy val root = (project in file(".")).
  settings(
    name := "tapir-zio-http4s",
    libraryDependencies ++= coreDependency,
    scalacOptions ++= Seq(
      "-deprecation", // Emit warning and location for usages of deprecated APIs.
      "-encoding", "utf-8", // Specify character encoding used by source files.
      "-explaintypes", // Explain type errors in more detail.
      "-feature", // Emit warning and location for usages of features that should be imported explicitly.
    )
  )

