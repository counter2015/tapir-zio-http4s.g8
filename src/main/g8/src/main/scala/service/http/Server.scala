package service.http

import cats.implicits._
import layer.{AllEnv, all}
import org.http4s.HttpRoutes
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import routes._
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import zio.interop.catz._
import zio.{ExitCode, RIO, URIO, ZIO}


object Server extends CatsApp {

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {

    val doc = SwaggerInterpreter().fromEndpoints[RIO[AllEnv, *]](
      PetRoutes.allEndpoints ++ FileRoutes.allEndpoints,
      "Our pets",
      "1.0")

    val swaggerRoutes: HttpRoutes[RIO[AllEnv, *]] =
      ZHttp4sServerInterpreter()
        .from(doc)
        .toRoutes

    val allRoutes = Router("/" ->(
        PetRoutes.allRoutes <+>
        FileRoutes.allRoutes <+>
        swaggerRoutes
      )).orNotFound


    val io = ZIO.runtime[AllEnv].flatMap { implicit runtime =>
      BlazeServerBuilder[RIO[AllEnv, *]]
        .withExecutionContext(runtime.platform.executor.asEC)
        .bindHttp(8080, "0.0.0.0")
        .withHttpApp(allRoutes)
        .serve
        .compile
        .drain
    }

    io
      .provideLayer(all)
      .exitCode
  }
}
