package fr.canal.rocket.zio.database.mongo

import fr.canal.rocket.zio.database.DataSource

import zio.{Ref, UIO, ULayer, ZLayer}

final case class DataSourceLive(ref: Ref[DatabaseContext]) extends DataSource:

  override def setCtx(ctx: DatabaseContext): UIO[Unit] = ref.set(ctx)
  override def getCtx: UIO[DatabaseContext] = ref.get

object DataSourceLive:

  def live: ULayer[DataSource] = ZLayer.scoped {
    for
      ref <- Ref.make[DatabaseContext](DatabaseContext.initial)
    yield DataSourceLive(ref)
  }
