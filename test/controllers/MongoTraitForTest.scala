package controllers

import reactivemongo.api.{MongoConnection, MongoDriver}
import scala.concurrent.ExecutionContext

trait MongoTraitForTest extends YetAnotherMongoTrait {
  protected[this] val executionContext: ExecutionContext

  protected[this] lazy val driver = new MongoDriver
  protected[this] lazy val connection = driver.connection(MongoConnection.parseURI("mongodb://localhost:27017/notover_test").get)
  protected[this] lazy val db = connection.db("notover_test")(executionContext)
}
