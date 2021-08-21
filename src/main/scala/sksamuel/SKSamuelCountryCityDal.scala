package sksamuel

import com.sksamuel.elastic4s.ElasticClient
import esUtils.CountryCityDal
import objects.{City, Country}
import objects.Country
import com.sksamuel.elastic4s.fields.TextField
import com.sksamuel.elastic4s.http.JavaClient
import com.sksamuel.elastic4s.requests.common.RefreshPolicy
import com.sksamuel.elastic4s.requests.searches.SearchResponse
import com.sksamuel.elastic4s.ElasticDsl._


import scala.concurrent.ExecutionContext
// circe
import com.sksamuel.elastic4s.circe._
import io.circe.generic.auto._

import scala.concurrent.Future

class SKSamuelCountryCityDal(esClient: ElasticClient, implicit val ec: ExecutionContext) extends CountryCityDal {

  override def loadAllCities(): Future[List[City]] = {
    esClient.execute {
      search(indexName)
        .query(
          boolQuery().must(termQuery("my_join_field", "city"))
        )
    }.map(_.map(_.to[City]).result.toList)

  }

  override def ingestCountries(countries: List[Country]): Future[List[Boolean]] = {

    val allReqs = countries.map(
      country => {
        indexInto(indexName).doc(country.json())
      }
    )
    esClient.execute {
      bulk(allReqs)
    }.map(bulkResp => {
      if (bulkResp.isError) {
        println("error", bulkResp.error) // TODO better handling.
      }
      List(bulkResp.result.errors)
    })
  }

  override def ingestCities(cities: List[City]): Future[List[Boolean]] = {
    val allReqs = cities.map(
      city => {
        indexInto(indexName).doc(city.json())
      }
    )
    esClient.execute {
      bulk(allReqs)
    }.map(bulkResp => {
      if (bulkResp.isError) {
        println("error", bulkResp.error) // TODO better handling.
      }
      List(bulkResp.result.errors)
    })
  }
}
