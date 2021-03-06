package controllers

import play.api.mvc._
import play.api.libs.json._
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.indexes.{ IndexType, Index }
import scala.concurrent.Future
import reactivemongo.core.commands.LastError
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import org.apache.commons.validator.routines.UrlValidator
import models.Note
import play.api.libs.json.JsArray
import reactivemongo.api.indexes.Index
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.json.JsObject
import play.api.mvc.SimpleResult

trait ApplicationController extends YetAnotherMongoTrait {
  self: Controller =>

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  private[this] def notesCollection = db.collection[JSONCollection]("notes")

  def createMongodbIndex = Action.async {
    notesCollection.indexesManager.ensure(Index(List("url" -> IndexType.Ascending))).map {
      status =>
        Ok(status.toString)
    }
  }

  private[this] def urlValidation(url: String)(successSequence: => Future[SimpleResult]) =
    if (new UrlValidator(Array("http", "https")).isValid(url))
      successSequence
    else
      Future.successful(BadRequest)

  def getNote(url: String) = Action.async {
    urlValidation(url) {
      val futureNotesList =
        notesCollection.find(Json.obj("url" -> url)).cursor[JsObject].collect[List]()

      val futureJson = futureNotesList.map(noteList => JsArray(noteList))
      futureJson.map(Ok(_))
    }
  }

  def postNote(url: String) = Action.async(parse.json) { request =>
    urlValidation(url) {
      request.body.validate[Note] match {
        case _: JsSuccess[Note] =>
          val futureLastError: Future[LastError] =
            notesCollection.save(request.body.as[JsObject] ++ Json.obj("url" -> url))

          futureLastError.map {
            lastError =>
              if (lastError.ok)
                Ok
              else
                InternalServerError
          }
        case _: JsError => Future.successful(BadRequest)
      }
    }
  }
}

object Application extends ApplicationController with Controller with MongoController
