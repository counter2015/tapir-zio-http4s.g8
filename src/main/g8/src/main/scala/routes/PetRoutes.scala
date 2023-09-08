package routes

import layer.AllEnv
import layer.PetLayer.PetService
import model.Pet
import org.http4s.HttpRoutes
import sttp.tapir.generic.auto._
import sttp.tapir.json.circe._
import sttp.tapir.server.http4s.ztapir.ZHttp4sServerInterpreter
import zio.ZIO
import sttp.tapir.ztapir._

object PetRoutes {

  val petEndpoint =
    endpoint
      .get
      .in("pet")
      .in(path[Int]("petId"))
      .errorOut(stringBody)
      .out(jsonBody[Pet])
      .tag("Pet")


  def petLogic(petId: Int): ZIO[PetService, String, Pet] = {
    PetService.find(petId)
  }

  val petRouter =
    petEndpoint
      .zServerLogic(petLogic)

  val allEndpoints = List(
    petEndpoint,
  )

  val allRoutes: HttpRoutes[ZIO[AllEnv, Throwable, *]] = ZHttp4sServerInterpreter().from(List(
    petRouter.widen[AllEnv],
  )).toRoutes
}
