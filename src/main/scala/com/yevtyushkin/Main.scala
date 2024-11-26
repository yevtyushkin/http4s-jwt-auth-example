package com.yevtyushkin

import cats.effect.{IO, Resource, ResourceApp}
import com.yevtyushkin.auth.{AuthMiddleware, AuthRoutes, AuthService}
import org.http4s.ember.server.EmberServerBuilder
import pureconfig.ConfigSource
import pureconfig.module.catseffect.syntax.*

object Main extends ResourceApp.Forever {
  def run(args: List[String]): Resource[IO, Unit] =
    for {
      config <- ConfigSource.default.loadF[IO, Config]().toResource

      authService <- AuthService
        .inMemory[IO](
          jwtSecret = config.jwtSecret,
          jwtExpirationTime = config.jwtExpirationTime
        )
        .toResource

      authMiddleware = AuthMiddleware[IO](
        jwtSecret = config.jwtSecret
      )

      authRoutes = AuthRoutes[IO](
        authService = authService,
        authMiddleware = authMiddleware
      )

      _ <- EmberServerBuilder
        .default[IO]
        .withPort(config.serverPort)
        .withHost(config.serverHost)
        .withHttpApp(authRoutes.orNotFound)
        .build
    } yield ()
}
