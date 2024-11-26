import Dependencies.*

ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.6.1"

lazy val root = (project in file("."))
  .settings(
    name := "http4s-jwt-auth-example",
    libraryDependencies ++= Seq(
      Http4s.EmberServer,
      Http4s.Dsl,
      Http4sJwtAuth,
      Pureconfig.CatsEffect,
      Pureconfig.Core,
      Pureconfig.Ip4s
    )
  )
