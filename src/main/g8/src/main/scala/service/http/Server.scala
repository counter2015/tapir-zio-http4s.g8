package service.http

import cats.implicits._
import layer.{AllEnv, all}
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import org.http4s.syntax.kleisli._
import routes.PetRoutes
import sttp.tapir.docs.openapi._
import sttp.tapir.openapi.circe.yaml.RichOpenAPI
import sttp.tapir.swagger.http4s.SwaggerHttp4s
import zio.interop.catz._
import zio.{ExitCode, RIO, URIO, ZIO}


object Server extends CatsApp {

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = {

    val doc = OpenAPIDocsInterpreter().toOpenAPI(
       PetRoutes.allEndpoints,
      "example-project",
      "0.0.1",
    ).toYaml

    val allRoutes = Router("/" ->(
        PetRoutes.allRoutes <+>
        new SwaggerHttp4s(doc).routes
      )).orNotFound


    val io = ZIO.runtime[AllEnv].flatMap { implicit runtime =>
      BlazeServerBuilder[RIO[AllEnv, *]](runtime.platform.executor.asEC)
        .bindHttp(8080, "localhost")
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
