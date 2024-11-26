package com.yevtyushkin.auth

import cats.Monad
import cats.syntax.all.*
import org.http4s.dsl.Http4sDsl
import org.http4s.server.AuthMiddleware
import org.http4s.{AuthedRoutes, HttpRoutes}

object AuthRoutes {

  def apply[F[_]: Monad](
      authService: AuthService[F],
      authMiddleware: AuthMiddleware[F, Username]
  ): HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl.*

    HttpRoutes
      .of[F] { case POST -> Root / "auth" / "signIn" / username =>
        for {
          signInResult <- authService.signIn(username)
          response <- signInResult match {
            case Left(e)          => BadRequest(e.toString)
            case Right(authToken) => Ok(authToken)
          }
        } yield response
      } <+> authMiddleware {
      AuthedRoutes.of[Username, F] { case GET -> Root / "auth" / "whoAmI" as username =>
        Ok(s"Hello, $username")
      }
    }
  }
}
