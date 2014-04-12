package controllers

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._

import play.api.test._
import play.api.test.Helpers._
import play.api.mvc.Controller

@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    "postNote" can {
      val controller = new ApplicationController with Controller with MongoTraitForTest {
        protected[this] val executionContext = scala.concurrent.ExecutionContext.global
      }
      val target = controller.getNote _

      "cannot accept file path" in {
        status(target("file://test.txt")(FakeRequest())) === OK
      }
    }
  }
}
