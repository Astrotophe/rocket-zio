package fr.canal.rocket.zio.database

enum DBError(val msg: String):

  case ReasonUnknown(override val msg: String) extends DBError(msg)
  case InvalidId(override val msg: String) extends DBError(msg)
  case NotFound(override val msg: String) extends DBError(msg)
