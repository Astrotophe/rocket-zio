package fr.canal.rocket.zio.database.mongo

import fr.canal.rocket.zio.database.{DBConfig, DataSource, DatabaseInitializer}
import org.mongodb.scala.*
import org.mongodb.scala.connection.ConnectionPoolSettings
import zio.{RIO, URLayer, ZIO, ZLayer}

import java.util.concurrent.TimeUnit

final case class MongoDatabaseInitializer(dataSource: DataSource) extends DatabaseInitializer:

  private val mongoServerSettings: ZIO[Any, Nothing, MongoClientSettings] = ZIO.succeed {
    MongoClientSettings.builder()
      .applyToClusterSettings(_.serverSelectionTimeout(5, TimeUnit.SECONDS))
      .build()
  }

  override def initialize(DBConfig: DBConfig): RIO[DataSource, Unit] =
    (for
      settings <- ZIO.log(
        s"""Attempting to establish the connection with MongoDB on port: ${DBConfig.port} with DB
           |${DBConfig.name}
           |""".stripMargin) *> mongoServerSettings
      client  <- ZIO.attempt(MongoClient(settings))
      db       <- ZIO.attempt(client.getDatabase(DBConfig.name))
      _        <- ZIO.fromFuture(implicit ec => db.listCollectionNames.toFuture).catchSome {
        case timeout: MongoTimeoutException => ZIO.fail(RuntimeException(s"Cannot connect to MongoDB: ${timeout.getMessage}."))
        case _ => ZIO.fail(RuntimeException("Cannot connect to MongoDB for an unknown reason."))
      } *> dataSource.setCtx(DatabaseContext(db))
    yield ()).orDie


object MongoDatabaseInitializer:

  lazy val live: URLayer[DataSource, DatabaseInitializer] = ZLayer.fromFunction(MongoDatabaseInitializer.apply)
