package fr.canal.rocket.zio.database.workflow

import fr.canal.rocket.zio.database.DataSource
import fr.canal.rocket.zio.domain.Domain.Workflow
import org.mongodb.scala.*
import org.mongodb.scala.model.Filters.*
import org.mongodb.scala.model.Sorts.*
import zio.{Task, UIO, URLayer, ZIO, ZLayer}

import java.time.Instant

final case class WorkflowRepositoryLive(dataSource: DataSource) extends WorkflowRepository:

  private val mongo: UIO[MongoDatabase] = dataSource.getCtx.map(_.mongoDatabase.get)

  override def getbyId(id: String): Task[Option[Workflow]] =
    for
      db            <- mongo
      doc: Document <- ZIO.fromFuture { implicit ec =>
        db.getCollection("workflows")
          .find(
            and(
              equal("id",id),exists("startedBy", true)
            )
          )
          .first()
          .toFuture()
      }
      maybedWorkflow <- ZIO.attempt(parseDocument(doc))
    yield maybedWorkflow

  private def parseDocument(doc: Document) =
    Option(doc).fold(None) { doc =>
      Some(
        Workflow(
          id        = doc("id").asString().getValue,
          name      = doc("name").asString().getValue,
          at        = Instant.ofEpochSecond(doc("at").asTimestamp().getTime),
          startedBy = doc("startedBy").asString().getValue,
          status    = doc("").asString().getValue
        )
      )
    }

object WorkflowRepositoryLive:

  lazy val live: URLayer[DataSource, WorkflowRepository] = ZLayer.fromFunction(WorkflowRepositoryLive.apply)