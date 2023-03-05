package fr.canal.rocket.zio.database

import zio.Task

trait Repository[A]:
  def getbyId(id: String): Task[Option[A]]

object Repository:
  type DBResult = Either[DBError, DBSuccess]