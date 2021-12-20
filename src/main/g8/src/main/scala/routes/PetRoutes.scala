package routes

import layer.AllEnv
import layer.PetLayer.PetService
import model.Pet
import org.http4s.HttpRoutes

import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import sttp.tapir.json.circe._
import sttp.tapir.generic.auto._
import sttp.tapir.ztapir._
import zio.ZIO
import zio.clock.Clock
import zio.logging.Logging

object PetRoutes {
  val petEndpoint: ZEndpoint[Int, String, Pet] =
    endpoint
      .get
      .in("pet")
      .in(path[Int]("petId"))
      .errorOut(stringBody)
      .out(jsonBody[Pet])

  def petLogic(petId: Int): ZIO[Logging with PetService, String, Pet] = {
    PetService.find(petId)
  }

  val petRouter: ZServerEndpoint[Logging with PetService, Int, String, Pet] =
    petEndpoint
      .zServerLogic(petLogic)

  val allEndpoints: List[ZEndpoint[_, _, _]] = List(
    petEndpoint,
  )

  val allRoutes: HttpRoutes[zio.ZIO[AllEnv with Clock, Throwable, *]] = ZHttp4sServerInterpreter().from(List(
    petRouter.widen[AllEnv],
  )).toRoutes
}
