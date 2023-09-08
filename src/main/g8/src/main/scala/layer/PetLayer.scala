package layer

import model.Pet
import zio._

object PetLayer {
  type PetService = PetService.Service

  object PetService {
    trait Service {
      def find(id: Int): ZIO[Any, String, Pet]
    }


    val live: ULayer[Service] = ZLayer.succeed {
      new Service {
        override def find(id: Int): ZIO[Any, String, Pet] = {
          for {
            _ <- ZIO.logInfo(s"Got request for pet: \$id")
            res <- if (id == 32) {
              ZIO.succeed(Pet("Tapirus terrestris", "https://en.wikipedia.org/wiki/Tapir"))
            } else {
              ZIO.fail("Unknown pet id")
            }
          } yield res
        }
      }
    }


    def find(id: Int): ZIO[PetService, String, Pet] = {
      for {
        service <- ZIO.service[PetService]
        res <- service.find(id)
      } yield res
    }
  }
}
