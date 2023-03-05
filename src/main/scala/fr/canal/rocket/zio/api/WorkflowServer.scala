package fr.canal.rocket.zio.api

import fr.canal.rocket.zio.api.endpoint.workflow.WorkflowEndpoints.{GetWorkflowError, getWorkflow}
import fr.canal.rocket.zio.configuration.Configuration
import fr.canal.rocket.zio.database.Repository
import fr.canal.rocket.zio.service.workflow.WorkflowService
import sttp.apispec.openapi.circe.yaml.*
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import sttp.tapir.swagger.SwaggerUI
import zio.http.{Http, HttpApp, Request, Response}
import zio.{Task, URLayer, ZIO, ZLayer}

/**
 * I renamed the trait to WorkflowServer
 * and renamed the companion object (removing the "Live" suffix)
 * actually the naming was messing with the compiler
 * (while doing Layers composition and dependency injection)
 * 
 * The layer pattern is a bit clearer this way
 * for further examples, you can also take inspiration from ZIO libs
 * (zio.http.Server was my inspiration here)
 */
trait WorkflowServer:
  
  def httpRoutes: ZIO[Any, Nothing, HttpApp[Any, Throwable]]


object WorkflowServer:

  def httpRoutes: ZIO[WorkflowServer, Nothing, HttpApp[Any, Throwable]] =
    ZIO.serviceWithZIO[WorkflowServer](_.httpRoutes)

  val live: URLayer[WorkflowService, WorkflowServerLive] = ZLayer {
    ZIO.service[WorkflowService].map(WorkflowServerLive.apply)
  }


case class WorkflowServerLive(workflowService: WorkflowService) extends WorkflowServer:

  private val allRoutes: Http[Any, Throwable, Request, Response] =
    ZioHttpInterpreter().toHttp(List(
      getWorkflow.serverLogic(
        userInfos => wfId =>
            for
              _           <- ZIO.cond(userInfos.roles.contains("ACTIVITY_READ"), (), GetWorkflowError.Authorization)
              workflow    <- workflowService.getWorkflowById(wfId)
                .mapError[GetWorkflowError](GetWorkflowError.UnexpectedError.apply)
                .someOrFail(GetWorkflowError.NotFound)
            yield  workflow
          )
      )
    )

  private val endpoints = List(
      getWorkflow
    ).map(_.endpoint)

  def httpRoutes: ZIO[Any, Nothing, HttpApp[Any, Throwable]] =
    // Note: You can use ServerLive.apply.toLayer if you want
    for
      openAPI       <- ZIO.succeed(OpenAPIDocsInterpreter().toOpenAPI(endpoints, "Workflow Service", "0.1"))
      routesHttp    <- ZIO.succeed(allRoutes)
      endpointHttp  <- ZIO.succeed(ZioHttpInterpreter().toHttp(SwaggerUI[Task](openAPI.toYaml)))
    yield routesHttp ++ endpointHttp