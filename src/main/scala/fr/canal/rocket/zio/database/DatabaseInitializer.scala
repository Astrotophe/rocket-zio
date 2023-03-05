package fr.canal.rocket.zio.database

import zio.RIO

trait DatabaseInitializer:

  def initialize(DBConfig: DBConfig): RIO[DataSource, Unit]