package controllers

import reactivemongo.api.{DB, MongoConnection, MongoDriver}

/**
 * 本家Play-ReactivemongoのMongoControllerおよびReactiveMongoPluginの
 * Playへの依存度が高すぎて、コントローラのテストが書きにくすぎるので、
 * 必要十分な機能を持った代わりのトレイトをここに定義した
 */
trait YetAnotherMongoTrait {
  def driver: MongoDriver
  def connection: MongoConnection
  def db: DB
}
