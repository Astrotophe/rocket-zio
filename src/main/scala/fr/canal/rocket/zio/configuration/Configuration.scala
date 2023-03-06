package fr.canal.rocket.zio.configuration

import fr.canal.rocket.zio.configuration.ConfigurationHandler.Config.{ApiConfig, MongoDBConfig}
import zio.config.magnolia.{describe, descriptor}
import zio.config.typesafe.TypesafeConfigSource
import zio.config.*
import ConfigDescriptor.*
import ConfigSource.*
import com.typesafe.config.ConfigFactory
import fr.canal.rocket.zio.configuration.ConfigurationHandler.Config
import zio.{RIO, Task, ZIO, ZLayer}


object ConfigurationHandler:

  enum Config:

    case MongoDBConfig(int: Int) extends Config
    case ApiConfig(
                    @describe("HTTP Webserver hotname")
                    host: String,
                    @describe("HTTP Webserver listen port")
                    port: Int
                  ) extends Config



trait Configuration:
  def api: Task[ApiConfig]
  def mongoDB: Task[MongoDBConfig]

object Configuration:

  def api: RIO[Configuration, ApiConfig] = ZIO.serviceWithZIO(_.api)
  def mongoDB : RIO[Configuration, MongoDBConfig] = ZIO.serviceWithZIO(_.mongoDB)

  val live = ZLayer.succeed(ConfigurationLive())

case class ConfigurationLive() extends Configuration:

  private lazy val apiConfig: ConfigDescriptor[ApiConfig] =
      nested("api")(string("host")).default("127.0.0.1")
        .zip(nested("api")(int("port")).default(80)).to[ApiConfig]

  private lazy val mongoDBConfig: ConfigDescriptor[MongoDBConfig] =
      int("to_be_done").default(1).to[MongoDBConfig]

  private def load[T <: Config](descriptor: ConfigDescriptor[T]): Task[T] =
      for
        source <- ZIO.attempt(TypesafeConfigSource
          .fromTypesafeConfig(ZIO.attempt(ConfigFactory.defaultApplication())))
        conf <- read(descriptor.from(source))
      yield conf

  override def api: Task[ApiConfig] = load(apiConfig)

  override def mongoDB: Task[MongoDBConfig] = load(mongoDBConfig)

