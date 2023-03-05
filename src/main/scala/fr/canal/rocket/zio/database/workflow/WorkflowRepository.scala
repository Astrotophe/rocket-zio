package fr.canal.rocket.zio.database.workflow

import fr.canal.rocket.zio.database.Repository
import fr.canal.rocket.zio.domain.Domain.Workflow

trait WorkflowRepository extends Repository[Workflow]
