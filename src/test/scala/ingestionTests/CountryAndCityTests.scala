package ingestionTests

import akka.actor.ActorSystem
import alpakka.CountryCityIngester
import objects.{City, Country}
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.scalatest.BeforeAndAfter
import org.scalatest.flatspec.AsyncFlatSpec

import scala.concurrent.{Await, Future}
import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.Duration


class CountryAndCityTests extends AsyncFlatSpec  with BeforeAndAfter {
  // Fixtures as reassignable variables and mutable objects

  implicit val restClient = RestClient.builder(new HttpHost("localhost", 9200)).build()

  implicit val actorSystem = ActorSystem("test")
  val ingester = new CountryCityIngester()

  before {
    val country = Country("USA", "United States", 3000000)
    val city = City("Bos", "Boston", 300000, "USA")

    ingester.ingestCountries(List(country))

    ingester.ingestCities(List(city))

    Thread.sleep(5000)

  }
  def addSoon(addends: Int*): Future[Int] = Future { addends.sum }

  it should "eventually compute a sum of passed Ints" in {
    val futureSum: Future[Int] = addSoon(1, 2)
    // You can map assertions onto a Future, then return
    // the resulting Future[Assertion] to ScalaTest:
    futureSum map { sum => assert(sum == 3) }
  }


}
