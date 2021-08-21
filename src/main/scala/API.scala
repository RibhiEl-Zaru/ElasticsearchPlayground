
import akka.actor.ClassicActorSystemProvider
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.server.Route

import scala.io.StdIn


object API extends AppLoader {

  def main(args: Array[String]): Unit = {

    // needed for the future flatMap/onComplete in the end
    implicit val actorSystem = ActorSystem(Behaviors.empty, "ES-API")

    implicit val executionContext = actorSystem.executionContext

    val allEndpoints = concat(
      adminEndpoints,
      esEndpoints
    )
    val bindingFuture = Http().newServerAt("localhost", 8080).bind(allEndpoints )

    println(s"Server now online. Please navigate to http://localhost:8081/hello\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => actorSystem.terminate()) // and shutdown when done
  }
}
