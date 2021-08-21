package alpakka

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.alpakka.elasticsearch.{ElasticsearchSourceSettings, ElasticsearchWriteSettings, ReadResult, WriteMessage, WriteResult}
import akka.stream.alpakka.elasticsearch.scaladsl.{ElasticsearchFlow, ElasticsearchSource}
import akka.stream.scaladsl.{Sink, Source}
import esUtils.CountryCityDal
import objects.{City, Country}
import org.elasticsearch.client.RestClient

import scala.collection.immutable
import scala.concurrent.Future

class AlpakkaCountryCityDal(implicit val actorSystem: ActorSystem, implicit val esClient: RestClient) extends CountryCityDal {


  val writeSettings = ElasticsearchWriteSettings.Default
  val materializer = ActorMaterializer()
  implicit val ec = actorSystem.dispatcher

  def ingestCountries(countries: List[Country]): Future[List[Boolean]] = {
    val reqs = countries.map(_.createWriteMessage())

    Source(reqs)
      .via(
        ElasticsearchFlow.create[Country](
          indexName,
          "_doc",
          writeSettings
        )
      ).runWith(Sink.seq)
      .map(_.map(_.success).toList) // TODO should probably add error handling but w/e for now.
  }

  def ingestCities(cities: List[City]): Future[List[Boolean]] = {

    val reqs = cities.map(_.createWriteMessage())

    Source(reqs)
      .via(
        ElasticsearchFlow.create[City](
          indexName,
          "_doc",
          writeSettings
        )
      ).runWith(Sink.seq)
      .map(_.map(_.success).toList)
  }

  def loadAllCities(): Future[List[City]] = {
    ElasticsearchSource.typed[City](
      indexName,
      None,
      searchParams = Map(
        "query" ->
          """ {
            |         "bool": {
            |            "must": [
            |                {
            |                    "term": {
            |                        "my_join_field": "city"
            |                    }
            |                }
            |            ]
            |        }
            |    } """.stripMargin
      ),
      ElasticsearchSourceSettings()
    ).runWith(Sink.seq)
      .map(_.map(_.source).toList)
  }


}
