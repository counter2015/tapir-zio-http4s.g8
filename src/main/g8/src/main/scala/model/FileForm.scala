package model

import java.io.File
import sttp.model.Part

case class FileForm(uploader: String, file: Part[File])
