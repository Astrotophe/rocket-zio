package fr.canal.rocket.zio.api.endpoint.admin

import sttp.tapir.json.zio.TapirJsonZio
import sttp.tapir.*

object AdminEndpoints extends TapirJsonZio:

  val healthCheck: PublicEndpoint[Unit, Unit, String, Any] =
    endpoint.name("Healthcheck").get.in("admin" / "healthcheck").out(stringBody("Service is alive !"))

