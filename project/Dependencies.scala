import sbt._

object Dependencies {
  import ProjectLibraries._

  val projectDependencies = Seq(zio, zioStreams, tapir, zioHttp, mongoDB, zioConfig, zioConfigTS, zioConfigMA,
    tapirSwagger, tapirOpenDoc, circeOpenAPI, tapirZioJson, tapirCirceJson, jwtZioJson, circeCore, circeGeneric, circeParser,
    zioTest, zioTestSbt, zioTestMagnolia)
}

object ProjectLibraries {
  val zio             = "dev.zio" %% "zio" % Versions.zio
  val zioStreams      = "dev.zio" %% "zio-streams" % Versions.zioStreams
  val zioConfig       = "dev.zio" %% "zio-config" % Versions.zioConfig
  val zioConfigTS     = "dev.zio" %% "zio-config-typesafe" % Versions.zioConfig
  val zioConfigMA     = "dev.zio" %% "zio-config-magnolia" % Versions.zioConfig
  val tapir           = "com.softwaremill.sttp.tapir" %% "tapir-core" % Versions.tapir
  val zioHttp         = "com.softwaremill.sttp.tapir" %% "tapir-zio-http-server" % Versions.tapir
  val mongoDB         = ("org.mongodb.scala" %% "mongo-scala-driver" % Versions.mongoDBVersion).cross(CrossVersion.for3Use2_13)
  val tapirZioJson    = "com.softwaremill.sttp.tapir" %% "tapir-json-zio" % Versions.tapirZioJson
  val tapirCirceJson  = "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % Versions.tapirCirceJson
  val tapirSwagger    = "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % Versions.tapir
  val tapirOpenDoc    = "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % Versions.tapir
  val circeOpenAPI    = "com.softwaremill.sttp.apispec" %% "openapi-circe-yaml" % Versions.openApiCirce
  val jwtZioJson      = "com.github.jwt-scala" %% "jwt-circe" % Versions.jtwZioJson
  val circeCore       = "io.circe" %% "circe-core" % Versions.circeVersion
  val circeGeneric    = "io.circe" %% "circe-generic" % Versions.circeVersion
  val circeParser     = "io.circe" %% "circe-core" % Versions.circeVersion


  val zioTest         = "dev.zio" %% "zio-test" % Versions.zio % Test
  val zioTestSbt      = "dev.zio" %% "zio-test-sbt" % Versions.zio % Test
  val zioTestMagnolia = "dev.zio" %% "zio-test-magnolia" % Versions.zio % Test

}

object Versions {
  val zio             = "2.0.5"
  val zioStreams      = "2.0.5"
  val zioConfig       = "3.0.1"
  val tapirZioJson    = "1.2.5"
  val tapir           = "1.2.5"
  val mongoDBVersion  = "2.9.0"
  val tapirCirceJson  = "1.2.7"
  val openApiCirce    = "0.3.1"
  val jtwZioJson      = "9.1.2"
  val circeVersion    = "0.14.1"
}