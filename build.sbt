ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.15"
val sparkVersion = "3.3.2"
lazy val root = (project in file("."))
  .settings(
    name := "practice1"
  )
libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion