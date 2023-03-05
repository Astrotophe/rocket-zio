package fr.canal.rocket.zio.service.workflow

import fr.canal.rocket.zio.api.endpoint.workflow.WorkflowEndpoints.GetWorkflowError
import fr.canal.rocket.zio.domain.Domain.Workflow
import zio.{Task, ZIO}

trait WorkflowService:

    def getWorkflowById(id: String): Task[Option[Workflow]]
