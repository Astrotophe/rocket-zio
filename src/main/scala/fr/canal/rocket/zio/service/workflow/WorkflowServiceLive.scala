package fr.canal.rocket.zio.service.workflow

import fr.canal.rocket.zio.database.workflow.WorkflowRepository
import fr.canal.rocket.zio.service.workflow.WorkflowService
import fr.canal.rocket.zio.domain.Domain.Workflow
import zio.{Task, URLayer, ZIO, ZLayer}


case class WorkflowServiceLive(repository: WorkflowRepository) extends WorkflowService:

  override def getWorkflowById(id: String): Task[Option[Workflow]] =
    repository.getbyId(id)

object WorkflowServiceLive:

  def live: URLayer[WorkflowRepository, WorkflowService] = ZLayer {
    for
      repository <- ZIO.service[WorkflowRepository]
    yield WorkflowServiceLive(repository)
  }

