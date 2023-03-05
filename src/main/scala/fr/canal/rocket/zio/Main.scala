package fr.canal.rocket.zio

import fr.canal.rocket.zio.api.ServerLive
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

 lazy val configurationLayers = ConfigurationLive.live

 lazy val dataSourceLayers = DataSourceLive.live
 lazy val reposLayers      = WorkflowRepositoryLive.live ++ MongoDatabaseInitializer.live

 // Vertical Composition: WorkflowService needs WorkflowRepo to be alive
 lazy val serviceLayers    =  reposLayers >>> WorkflowServiceLive.live

 val program: ZIO[Any, Throwable, Unit] =
  for
   _         <- ZIO.log("Workflow Status API")
   api       <- ConfigurationLive.api
   httpApp   <- ServerLive.live()
   toto       <- httpApp
   start     <- Server.install()
   _         <- ZIO.log(s"API Loaded Configuration: ${api.host}, ${api.port}")
   _         <- ZIO.never
  yield()

 def run = program
   .provideLayer(
     serviceLayers,
     ZLayer.Debug.tree
   )

