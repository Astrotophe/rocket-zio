package fr.canal.rocket.zio.api

import zio.ZIO
import zio.http.HttpApp
import sttp.tapir.server.interceptor.log.DefaultServerLog
import zio.{Console, ExitCode, Scope, Task, ZIO}
import sttp.tapir.server.ziohttp.{ZioHttpInterpreter, ZioHttpServerOptions}

trait WorkflowServer:
  def httpRoutes: ZIO[Any, Nothing, HttpApp[Any, Throwable]]

