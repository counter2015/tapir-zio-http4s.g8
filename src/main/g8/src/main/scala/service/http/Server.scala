package service.http

import cats.implicits._
import layer.{AllEnv, all}
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.syntax.kleisli._
import routes._
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.tapir.openapi.circe.yaml.RichOpenAPI
import sttp.tapir.swagger.http4s.SwaggerHttp4s
import zio.interop.catz._
import zio.{ExitCode, RIO, URIO, ZIO}

object Server extends CatsApp {

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {

    val doc = OpenAPIDocsInterpreter
      .toOpenAPI(
        PetRoutes.allEndpoints ++ FileRoutes.allEndpoints,
        "Our pets",
        "1.0")
      .toYaml

    val allRoutes = Router("/" ->(
        PetRoutes.allRoutes <+>
        FileRoutes.allRoutes <+>
          new SwaggerHttp4s(doc).routes
      )).orNotFound

    val io = ZIO.runtime[AllEnv].flatMap { implicit runtime =>
      BlazeServerBuilder[RIO[AllEnv, *]](runtime.platform.executor.asEC)
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
