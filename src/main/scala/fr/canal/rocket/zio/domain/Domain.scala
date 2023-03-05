package fr.canal.rocket.zio.domain

import zio.json.{DeriveJsonDecoder, DeriveJsonEncoder, JsonDecoder, JsonEncoder}

import java.time.Instant

object Domain {

  final case class Workflow(id: String, name: String, status: String, at: Instant, startedBy: String)

  object Workflow:

    given decoder: JsonDecoder[Workflow] = DeriveJsonDecoder.gen[Workflow]
    given encoder: JsonEncoder[Workflow] = DeriveJsonEncoder.gen[Workflow]

    def apply(id: String, name: String, status: String, at: Instant, startedBy: String): Workflow = Workflow(id, name,
      status, at, startedBy)

}