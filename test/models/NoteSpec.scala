package models

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.libs.json.{ JsError, JsSuccess, Json }

@RunWith(classOf[JUnitRunner])
class NoteSpec extends Specification {

  "Note" should {
    "successes correct json" in {
      Json.obj("selector" -> "a", "text" -> "b").validate[Note] must beLike {
        case JsSuccess(Note("a", "b"), _) => ok
      }
    }

    "errors incorrect json" in {
      Json.obj("incorrect" -> "structure").validate[Note] must beLike {
        case JsError(_) => ok
      }
    }
  }
}
