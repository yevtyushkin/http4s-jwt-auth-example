package com.yevtyushkin.auth

import cats.MonadThrow
import cats.syntax.all.*
import com.yevtyushkin.Config.SecretConfigValue
import dev.profunktor.auth.JwtAuthMiddleware
import dev.profunktor.auth.jwt.JwtAuth
import org.http4s.server.AuthMiddleware
import pdi.jwt.{JwtAlgorithm, JwtClaim}

object AuthMiddleware {
  def apply[F[_]: MonadThrow](
      jwtSecret: SecretConfigValue[String]
  ): AuthMiddleware[F, Username] = {

    val jwtAuth = JwtAuth.hmac(
      secretKey = jwtSecret.value.toCharArray,
      algorithm = JwtAlgorithm.HS512
    )

    JwtAuthMiddleware[F, Username](
      jwtAuth = jwtAuth,
      authenticate = _ => (claim: JwtClaim) => claim.subject.pure[F]
    )
  }
}
