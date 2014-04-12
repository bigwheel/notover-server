name := "notover-server"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.2",
  "commons-validator" % "commons-validator" % "1.4.0"
)     

play.Project.playScalaSettings

scalariformSettings
