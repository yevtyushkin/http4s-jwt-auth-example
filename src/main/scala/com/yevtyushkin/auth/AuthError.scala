package com.yevtyushkin.auth

import scala.util.control.NoStackTrace

enum AuthError extends NoStackTrace {
  case UserAlreadyExists
}
