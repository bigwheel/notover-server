package controllers

import play.api.mvc._
import play.api.libs.json.{JsArray, JsObject, Json}
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.indexes.{IndexType, Index}
import scala.concurrent.Future
import reactivemongo.core.commands.LastError

trait ApplicationController extends YetAnotherMongoTrait {
  self: Controller =>

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

  def getNote(url: String) = Action.async {
    val futureNotesList =
      notesCollection.find(Json.obj("url" -> url)).cursor[JsObject].collect[List]()

    val futureJson = futureNotesList.map (noteList => JsArray(noteList))
    futureJson.map(Ok(_))
  }

  def postNote(url: String) = Action.async(parse.json) { request =>
    val futureLastError: Future[LastError] = notesCollection.save(request.body.as[JsObject] ++ Json.obj("url" -> url))

    futureLastError.map { lastError =>
      if (lastError.ok)
        Ok
      else
        BadRequest
    }
  }
}

object Application extends ApplicationController with Controller with MongoController {
  protected[this] implicit val executionContext = play.api.libs.concurrent.Execution.Implicits.defaultContext
}