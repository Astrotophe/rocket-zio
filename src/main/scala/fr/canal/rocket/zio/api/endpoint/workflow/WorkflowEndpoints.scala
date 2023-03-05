package fr.canal.rocket.zio.api.endpoint.workflow

import fr.canal.rocket.zio.api.security.Security
import fr.canal.rocket.zio.domain.Domain.Workflow
import io.circe.generic.auto.*
import sttp.capabilities.zio.ZioStreams
import sttp.model.StatusCode
import sttp.tapir.generic.Derived
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.TapirJsonCirce
import sttp.tapir.ztapir.*
import sttp.tapir.{Schema, SchemaType}

object WorkflowEndpoints extends TapirJsonCirce with Security:

  enum GetWorkflowError:
    case Authentication extends GetWorkflowError
    case Authorization extends GetWorkflowError
    case NotFound extends GetWorkflowError
    case UnexpectedError(throwable: Throwable) extends  GetWorkflowError

  given workflowSchema: Schema[Workflow] =  Schema.derived[Workflow]

  def getWorkflow: ZPartialServerEndpoint[Any, BearerToken, UserInfos, String, GetWorkflowError, Workflow, ZioStreams] =
    secureEndpoint
      .name("Get workflow infos")
      .get
      .in("workflows" / path[String]("workflowId"))
      .out(jsonBody[Workflow])
      .mapErrorOut(_ => GetWorkflowError.Authentication)(_ => AuthenticationError(1001))
      .errorOutVariants[GetWorkflowError](
        oneOfVariant(statusCode(StatusCode.Unauthorized).and(emptyOutputAs(GetWorkflowError.Authentication))),
        oneOfVariant(statusCode(StatusCode.Forbidden).and(emptyOutputAs(GetWorkflowError.Authorization))),
        oneOfVariant(statusCode(StatusCode.NotFound).and(emptyOutputAs(GetWorkflowError.NotFound)))
      )