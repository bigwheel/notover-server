package models

import play.api.libs.json.OFormat
import play.jsonext.CaseClassFormats

final case class Note(sections: Set[Section])

object Note {
  implicit val NoteOFormat: OFormat[Note] =
    CaseClassFormats(apply _, unapply _)("sections")
}

final case class Section(selector: String, text: String)

object Section {
  implicit val SectionOFormat: OFormat[Section] =
    CaseClassFormats(apply _, unapply _)("selector", "text")
}
