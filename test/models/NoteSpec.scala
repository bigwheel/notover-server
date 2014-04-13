package models

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.libs.json.{ JsError, JsSuccess, Json }

@RunWith(classOf[JUnitRunner])
class NoteSpec extends Specification {

  "Note" should {
    "successes correct json" in {
      val sampleJson = Json.obj(
        "sections" -> Seq(
          Json.obj("selector" -> "a", "text" -> "b"),
          Json.obj("selector" -> "c", "text" -> "d")
        )
      )
      sampleJson.validate[Note] must beLike {
        case JsSuccess(Note(sections), _) if sections == Set(Section("c", "d"), Section("a", "b")) => ok
      }
    }

    "errors incorrect json" in {
      Json.obj("incorrect" -> "structure").validate[Note] must beLike {
        case JsError(_) => ok
      }
    }
  }
}
