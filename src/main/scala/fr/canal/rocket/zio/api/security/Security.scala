package fr.canal.rocket.zio.api.security

import io.circe.Decoder.Result
import io.circe.{Decoder, HCursor}
import pdi.jwt.JwtCirce
import sttp.tapir.ztapir.*
import zio.ZIO

trait Security:

  final case class UserInfos(username: String, roles: Vector[String])

  given decodeUserInfos: Decoder[UserInfos] =
    new Decoder[UserInfos]:
      override final def apply(c: HCursor): Result[UserInfos] =
        for
          username <- c.downField("content").downField("username").as[String]
          roles    <- c.downField("content").downField("realm_access").downField("roles").as[Vector[String]]
        yield UserInfos(username, roles)

  opaque type BearerToken = String
  extension (bearer: BearerToken)
    def value: String = bearer

  object BearerToken:
    def apply(bearer: String): BearerToken = bearer

  case class AuthenticationError(value: Int)

  private def decodeUserInfosfromJwt(jwtToken: String): Either[Throwable, UserInfos] =
    JwtCirce.decodeJson(jwtToken).toEither.flatMap(_.as[UserInfos])

  def authenticate(token: BearerToken): ZIO[Any, AuthenticationError, UserInfos] =
    decodeUserInfosfromJwt(token.value) match
      case Left(_) => ZIO.fail(AuthenticationError(1001))
      case Right(value) => ZIO.succeed(value)


  def secureEndpoint: ZPartialServerEndpoint[Any, BearerToken, UserInfos, Unit, AuthenticationError, Unit, Any] =
    endpoint
      .securityIn(auth.bearer[String]().map(BearerToken(_))(_.value))
      .errorOut(emptyOutputAs(AuthenticationError(1001)))
      .zServerSecurityLogic(token => authenticate(token))




