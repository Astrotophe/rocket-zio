package fr.canal.rocket.zio.database

enum DBSuccess(val msg: String):

  case Created(override val msg: String) extends DBSuccess(msg)
  case Updated(override val msg: String) extends DBSuccess(msg)
  case Deleted(override val msg: String) extends DBSuccess(msg)