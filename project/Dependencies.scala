import sbt.*

object Dependencies {

  object Http4s {
    private val Version = "0.23.29"

    val EmberServer = "org.http4s" %% "http4s-ember-server" % Version
    val Dsl         = "org.http4s" %% "http4s-dsl"          % Version
  }

  val Http4sJwtAuth = "dev.profunktor" %% "http4s-jwt-auth" % "2.0.1"

  object Pureconfig {
    val Version = "0.17.8"

    val CatsEffect = "com.github.pureconfig" %% "pureconfig-cats-effect" % Version
    val Core       = "com.github.pureconfig" %% "pureconfig-core"        % Version
    val Ip4s       = "com.github.pureconfig" %% "pureconfig-ip4s"        % Version
  }
}
