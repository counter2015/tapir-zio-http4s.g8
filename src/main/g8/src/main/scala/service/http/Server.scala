package service.http

import cats.implicits._
import layer.{AllEnv, LoggingLayer, all}
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import routes._
import sttp.tapir.redoc.bundle.RedocInterpreter
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import zio.interop.catz._
import zio._



object Server extends ZIOAppDefault {

  override def run = {

    val doc = RedocInterpreter()
      .fromEndpoints[RIO[AllEnv, *]](PetRoutes.allEndpoints ++ FileRoutes.allEndpoints, "Our pets", "1.0")

    val docRoutes: HttpRoutes[ZIO[AllEnv, Throwable, *]] = ZHttp4sServerInterpreter().from(doc).toRoutes


    val allRoutes = Router("/" ->(
      PetRoutes.allRoutes <+>
        FileRoutes.allRoutes <+>
        docRoutes
      )).orNotFound

    val io = ZIO.runtime[AllEnv].flatMap { _ =>
      for {
        _ <- ZIO.executor.flatMap(executor =>
          BlazeServerBuilder[RIO[AllEnv, *]]
            .withExecutionContext(executor.asExecutionContext)
            .bindHttp(8080, "0.0.0.0")
            .withHttpApp(allRoutes)
            .serve
            .compile
            .drain)
      } yield ()
    }

    io
      .provideLayer(all ++ LoggingLayer.live)
      .exitCode
  }
}
