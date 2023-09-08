package layer

import zio._
import zio.logging.backend.SLF4J

object LoggingLayer {
  val live = Runtime.removeDefaultLoggers >>> SLF4J.slf4j
}
