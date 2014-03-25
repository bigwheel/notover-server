package controllers

import play.api.mvc._
import play.api.libs.json.{JsObject, Json}
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import reactivemongo.api.indexes.{IndexType, Index}

object Application extends Controller with MongoController {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def notesCollection = db.collection[JSONCollection]("notes")

  def createMongodbIndex = Action.async {
    notesCollection.indexesManager.ensure(Index(List("url" -> IndexType.Ascending))).map {
      status =>
        Ok(status.toString)
    }
  }

  def notes(url: String) = Action.async {
    val futureNotesList =
      notesCollection.find(Json.obj("url" -> url)).cursor[JsObject].collect[List]()

    val futureJson = futureNotesList.map { notesList =>
      notesList.headOption match {
        case Some(jsObject) => jsObject
        case None => Json.obj()
      }
    }
    futureJson.map(Ok(_))
  }

}