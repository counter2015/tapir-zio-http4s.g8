import layer.PetLayer.PetService
import zio.{ULayer, ZEnv, ZLayer}
import zio.logging.Logging

package object layer {
  type AllEnv = PetService with ZEnv with Logging

  val petLayer: ZLayer[Logging, String, PetService] = PetService.live

  val loggingLayer: ULayer[Logging] = LoggingLayer.live

  val all: ZLayer[Any, String, AllEnv] = ZEnv.live >+> loggingLayer >+> petLayer
}
