package routes

import layer.AllEnv
import model.FileForm
import org.http4s.HttpRoutes
import sttp.tapir.generic.auto._
import sttp.tapir.PublicEndpoint
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import sttp.tapir.ztapir._
import zio.ZIO



object FileRoutes {

  val uploadEndpoint: PublicEndpoint[(String, FileForm), Unit, String, Any] =
    endpoint
      .post
      .in("file")
      .in(path[String]("id"))
      .in(multipartBody[FileForm])
      .out(stringBody)
      .tag("File")

  def uploadLogic(id: String, form: FileForm): ZIO[Any, Nothing, String] = {
    for {
      _ <- ZIO.logInfo(s"got upload file request, id is \$id")
    } yield form.file.fileName.getOrElse("file name not found")
  }

  val uploadRouter= uploadEndpoint.zServerLogic((uploadLogic _).tupled)

  val allEndpoints = List(
    uploadEndpoint,
  )

  val allRoutes: HttpRoutes[ZIO[AllEnv, Throwable, *]] = ZHttp4sServerInterpreter().from(List(
    uploadRouter.widen[AllEnv],
  )).toRoutes
}
