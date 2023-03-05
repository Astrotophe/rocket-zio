package fr.canal.rocket.zio.database

import fr.canal.rocket.zio.database.mongo.DatabaseContext
import zio.UIO

trait DataSource:

  def setCtx(ctx: DatabaseContext): UIO[Unit]
  def getCtx: UIO[DatabaseContext]