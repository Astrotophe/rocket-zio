package fr.canal.rocket.zio

import fr.canal.rocket.zio.database.Repository.DBResult
import org.mongodb.scala.result.{DeleteResult, UpdateResult}
import zio.*

package object database:

  extension[A <: DeleteResult | UpdateResult] (self: A)
    def fold(wasAcknowledged: => Boolean, onSuccess: => DBSuccess, onFailure: => DBError): UIO[DBResult] =
      ZIO.succeed(if wasAcknowledged then Right(onSuccess) else Left(onFailure))

