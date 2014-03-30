package controllers

import reactivemongo.api.{DB, MongoConnection, MongoDriver}
import scala.concurrent.ExecutionContext

/**
 * 本家Play-ReactivemongoのMongoControllerおよびReactiveMongoPluginの
 * Playへの依存度が高すぎて、コントローラのテストが書きにくすぎるので、
 * 必要十分な機能を持った代わりのトレイトをここに定義した
 */
trait YetAnotherMongoTrait {
  protected[this] implicit val executionContext: ExecutionContext
  protected[this] def driver: MongoDriver
  protected[this] def connection: MongoConnection
  protected[this] def db: DB
}
