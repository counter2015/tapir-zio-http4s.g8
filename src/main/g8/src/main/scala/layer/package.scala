import layer.PetLayer.PetService
import zio._

package object layer {
  type AllEnv = PetService

  val petLayer: ZLayer[Any, String, PetService] = PetService.live

  val loggingLayer  = LoggingLayer.live

  val all: ZLayer[Any, String, AllEnv] = petLayer
}
