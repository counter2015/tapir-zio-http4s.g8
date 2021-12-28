package model

import io.circe.generic.JsonCodec

@JsonCodec
case class Pet(species: String, url: String)
