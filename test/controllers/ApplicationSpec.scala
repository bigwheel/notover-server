package controllers

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import play.api.mvc._
import play.api.libs.json.Json

@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification with org.specs2.matcher.DataTables {

  "Application" should {

    val controller = new ApplicationController with Controller with MongoTraitForTest {
      protected[this] val executionContext = scala.concurrent.ExecutionContext.global
    }

    "getNote" >> {
      val target = controller.getNote _

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
            status(target(sample_url)(FakeRequest())) must_== status_code
          }
      }
    }

    "postNote" >> {
      val target = controller.postNote _

      "accepts only http and https protocols" in {
        "status code" | "sample url" |
          OK ! "http://www.google.com/" |
          OK ! "https://www.google.com/" |
          BAD_REQUEST ! "ftp://www.google.com/" |
          BAD_REQUEST ! "file://www.google.com/" |> { (statusCode, sampleUrl) =>
            val req = new FakeRequest(POST, controllers.routes.Application.postNote("").url,
              FakeHeaders(), Json.obj())
            // ↑ なぜか URL 取得するのにダミーの引数渡さないといけないださい仕様だが play ゆえ致し方なし
            status(target(sampleUrl)(req)) must_== statusCode
          }
      }

      "accepts empty json" in {
        val req = new FakeRequest(POST, controllers.routes.Application.postNote("").url,
          FakeHeaders(), Json.obj())
        status(target("http://www.google.com/")(req)) must_== OK
      }

      "doesn't accept incorrect format json" in {
        val req = new FakeRequest(POST, controllers.routes.Application.postNote("").url,
          FakeHeaders(), Json.obj("bad" -> "json"))
        status(target("http://www.google.com/")(req)) must_== BAD_REQUEST
      }
    }
  }
}
