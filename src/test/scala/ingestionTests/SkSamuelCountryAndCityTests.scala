package ingestionTests

import akka.actor.ActorSystem
import alpakka.AlpakkaCountryCityDal
import com.sksamuel.elastic4s.{ElasticClient, ElasticProperties}
import com.sksamuel.elastic4s.http.JavaClient
import objects.{City, Country}
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.scalatest.BeforeAndAfter
import org.scalatest.flatspec.AsyncFlatSpec
import sksamuel.SKSamuelCountryCityDal

import scala.concurrent.{ExecutionContext, Future}


class SkSamuelCountryAndCityTests extends AsyncFlatSpec  with BeforeAndAfter {
  // Fixtures as reassignable variables and mutable objects

  val props = ElasticProperties("http://localhost:9200")
  val client = ElasticClient(JavaClient(props))

  implicit val actorSystem = ActorSystem("test")
  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.global
  val ingester = new SKSamuelCountryCityDal(client, ec)


  before {
    val country = Country("USA", "United States", 3000000)
    val city = City("Bos", "Boston", 300000, "USA")

    ingester.ingestCountries(List(country))

    ingester.ingestCities(List(city))

    Thread.sleep(5000)

  }
  def addSoon(addends: Int*): Future[Int] = Future { addends.sum }

  it should "be able to load all cities" in {

    ingester.loadAllCities().map(x => {
      assert(x.size == 1)
    })
    val futureSum: Future[Int] = addSoon(1, 2)
    // You can map assertions onto a Future, then return
    // the resulting Future[Assertion] to ScalaTest:

    Thread.sleep(5000)
    futureSum map { sum => {
      assert(sum == 3)
    } }

  }


}
