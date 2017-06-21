package liauw.scalajs.jvm.server

import akka.actor._
import akka.http.scaladsl._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.stream._
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._

object Server {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()
  implicit val timeout: Timeout =  5.seconds

  val route =
    path("") {
      redirect("index.html", StatusCodes.PermanentRedirect)
    } ~
      path("index.html") {
        get {
          getFromResource("index.html")
        }
      } ~
      pathPrefix("resources") (getFromDirectory("js/target/scala-2.12/"))

  def main(args: Array[String]): Unit = {
    Await.result(Http().bindAndHandle(route, "127.0.0.1", 8080), 3.seconds)
    println("Started server at 127.0.0.1:8080, press enter to kill server")
  }
}
