package controllers

import play.api.mvc._
import play.api.libs.json.Json

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def notes(url: String) = Action {
    Ok(Json.obj())
  }

}