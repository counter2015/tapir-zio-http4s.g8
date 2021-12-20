package model

import io.circe.generic.JsonCodec
import sttp.tapir.Schema
import sttp.tapir.generic.auto.schemaForCaseClass

@JsonCodec
case class Pet(species: String, url: String)


