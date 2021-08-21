package EndpointSuites

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, ResponseEntity, StatusCode}
import akka.http.scaladsl.server.Directives.{complete, concat, get, path, pathPrefix}
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer
import alpakka.CountryCityDal
import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder
import objects.{City, Country}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

class AlpakkaEndpointsSuite(esDal: CountryCityDal)(implicit val ec: ExecutionContext, implicit val system: ActorSystem) {

  implicit val materializer = ActorMaterializer()

  import akka.http.scaladsl.server.Directives._
  import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
  import io.circe.generic.auto._

  val decoder: Decoder[List[City]] = deriveDecoder[List[City]]

  val endpoints = {
    pathPrefix("alpakka") {
      concat(
        path("cities") {
          get {
            val cities = Await.result(getCities(), Duration.Inf)
            complete(200, Unmarshal(cities))
          }
        }
      )
    }
  }


  def getCities(): Future[List[City]] = {
    esDal.loadAllCities()
  }


}
