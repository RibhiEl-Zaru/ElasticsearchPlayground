import EndpointSuites.{AdminEndpointsSuite, AlpakkaEndpointsSuite}
import akka.actor.ActorSystem
import alpakka.AlpakkaCountryCityDal
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient

import scala.concurrent.ExecutionContext.Implicits.global // where we get the EC.

trait AppLoader {

  // TODO make stuff configurable
  implicit val restClient = RestClient.builder(new HttpHost("localhost", 9200)).build()
  implicit val actorSystem = ActorSystem("ES-API")

  private val adminEndpointsSuite = new AdminEndpointsSuite
  private val esDal: AlpakkaCountryCityDal = new AlpakkaCountryCityDal()

  private val alpakkaEndpointsSuite = new AlpakkaEndpointsSuite(esDal)

  val adminEndpoints = adminEndpointsSuite.endpoints
  val esEndpoints = alpakkaEndpointsSuite.endpoints


}
