package models

import play.api.libs.json.OFormat
import play.jsonext.CaseClassFormats

final case class Note(selector: String, text: String)

object Note {
  implicit val fooOFormat: OFormat[Note] =
    CaseClassFormats(apply _, unapply _)(
      "selector", "text"
    )
}
