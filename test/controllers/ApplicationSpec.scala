package controllers

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import play.api.mvc._
import play.api.libs.json.{ Json, JsValue }

@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification with org.specs2.matcher.DataTables {

  "Application" should {

    "postNote" >> {
      val controller = new ApplicationController with Controller with MongoTraitForTest {
        protected[this] val executionContext = scala.concurrent.ExecutionContext.global
      }
      val target = controller.postNote _

      "accepts only http and https protocols" in {
        // ↓ クソイケてない specs2 の data table のせいで data table の１列目に文字列を持ってくることができない。
        // 本当は sample url, status code の順番で書きたいのにやむなくこうしてる
        // たぶん DSL 使わず型を直接書いたら行けるけど、
        // そうなると for で十分なのでなんのために Data Table 使ってるのかわからん
        "status code" | "sample url" |
          OK ! "http://www.google.com/" |
          OK ! "https://www.google.com/" |
          BAD_REQUEST ! "ftp://www.google.com/" |
          BAD_REQUEST ! "file://www.google.com/" |> { (status_code, sample_url) =>
            val req: FakeRequest[JsValue] = new FakeRequest(POST,
              controllers.routes.Application.postNote("").url, FakeHeaders(),
              Json.obj() // ↑ なぜか URL 取得するのにダミーの引数渡さないといけないださい仕様だが play ゆえ致し方なし
            )
            status(target(sample_url)(req)) === status_code
          }
      }
    }
  }
}
