package com.yevtyushkin.auth

import cats.effect.kernel.Sync
import cats.effect.{Concurrent, Ref}
import cats.syntax.all.*
import com.yevtyushkin.Config.SecretConfigValue
import pdi.jwt.{Jwt, JwtAlgorithm, JwtClaim}

import scala.concurrent.duration.FiniteDuration

trait AuthService[F[_]] {
  def signIn(username: Username): F[Either[AuthError, AuthToken]]
}

object AuthService {

  def inMemory[F[_]: Sync](
      jwtSecret: SecretConfigValue[String],
      jwtExpirationTime: FiniteDuration
  ): F[AuthService[F]] =
    for {
      usersRef <- Ref.of[F, Set[Username]](Set.empty)
    } yield new AuthService[F] {
      def signIn(username: Username): F[Either[AuthError, AuthToken]] =
        for {
          // Some dummy auth logic
          userAlreadyExists <- usersRef.modify { users =>
            val alreadyExists = users.contains(username)
            (users + username, alreadyExists)
          }

          now <- Sync[F].realTimeInstant

          authToken <- Sync[F].delay {
            val issuedAtSeconds = now.getEpochSecond

            val claim = JwtClaim(
              subject = username.some,
              issuedAt = issuedAtSeconds.some,
              expiration = (issuedAtSeconds + jwtExpirationTime.toSeconds).some
            )

            Jwt.encode(
              claim = claim,
              algorithm = JwtAlgorithm.HS512,
              key = jwtSecret.value
            )
          }

        } yield
          if (userAlreadyExists) AuthError.UserAlreadyExists.asLeft
          else authToken.asRight
    }
}
