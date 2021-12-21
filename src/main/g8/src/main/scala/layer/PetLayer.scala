package layer

import model.Pet
import zio._
import zio.logging.{Logger, Logging}

object PetLayer {
  type PetService = Has[PetService.Service]

  object PetService {
    trait Service {
      def find(id: Int): ZIO[Logging, String, Pet]
    }

    val live: ZLayer[Logging, String, Has[Service]] = ZLayer.fromService[Logger[String], Service]{  logging =>
      new Service {
        override def find(id: Int): ZIO[Logging, String, Pet] = {
          for {
            _ <- logging.info(s"Got request for pet: \$id")
            res <- if (id == 32) {
              UIO(Pet("Tapirus terrestris", "https://en.wikipedia.org/wiki/Tapir"))
            } else {
              IO.fail("Unknown pet id")
            }
          } yield res
        }
      }
    }

    def find(id: Int): ZIO[Logging with PetService, String, Pet] = ZIO.accessM(_.get[PetService.Service].find(id))
  }
}
