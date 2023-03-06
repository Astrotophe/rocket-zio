package fr.canal.rocket.zio

import fr.canal.rocket.zio.api.WorkflowServer
import fr.canal.rocket.zio.configuration.*
import fr.canal.rocket.zio.database.mongo.{DataSourceLive, MongoDatabaseInitializer}
import fr.canal.rocket.zio.database.workflow.WorkflowRepositoryLive
import fr.canal.rocket.zio.service.workflow.WorkflowServiceLive
import zio.*
import zio.http.*

/**
 * ZLayer Dependency Graph
 *
 *
 *
 */
object Main extends ZIOAppDefault:

  /**
   * As you have an all-in-one "program",
   * you need to specify the input type
   * to be able to provide the config layers (Configuration, WorkflowServer)
   * and use the dependency injection (Reader Monad ftw)
   */
  private val program: RIO[Configuration & WorkflowServer, Unit] =
    for
      _         <- ZIO.log("Workflow Status API")
      api       <- Configuration.api
      httpApp   <- WorkflowServer.httpRoutes
      _         <- ZIO.log(s"API Documentation: http://localhost:${api.port}/docs")
      config     = ServerConfig.default.port(api.port)
      server    <- Server.serve(httpApp).provide(ServerConfig.live(config), Server.live)
    yield ()

  // As you are using ZIO 2
  // you need to provide the layers using the ZIO#provide method
  // no need to specify the composition hierarchy
  // ZIO#provide will do it for you (thanks to the source project: https://github.com/kitlangton/zio-magic)
  def run = program
    .provide(
      // configutation layers
      Configuration.live,
      WorkflowServer.live,

      // service layers
      WorkflowServiceLive.live,

      // datasource layers
      DataSourceLive.live,

      // repos layers
      WorkflowRepositoryLive.live,
      // MongoDatabaseInitializer.live, // not needed yet, as no service nor layer is using it for now
    )
