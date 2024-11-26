package com.yevtyushkin

import com.comcast.ip4s.*
import com.yevtyushkin.Config.SecretConfigValue
import pureconfig.ConfigReader
import pureconfig.module.ip4s.*

import scala.concurrent.duration.FiniteDuration

case class Config(
    serverPort: Port,
    serverHost: Host,
    jwtSecret: SecretConfigValue[String],
    jwtExpirationTime: FiniteDuration
) derives ConfigReader

object Config {

  case class SecretConfigValue[T](value: T) extends AnyVal {
    override def toString: String = "secret"
  }

  object SecretConfigValue {
    given [T](using ConfigReader[T]): ConfigReader[SecretConfigValue[T]] = ConfigReader[T].map(SecretConfigValue.apply)
  }
}
