package alpakka

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.alpakka.elasticsearch.{ElasticsearchSourceSettings, ElasticsearchWriteSettings, ReadResult, WriteMessage, WriteResult}
import akka.stream.alpakka.elasticsearch.scaladsl.{ElasticsearchFlow, ElasticsearchSource}
import akka.stream.scaladsl.{Sink, Source}
import objects.{City, Country}
import org.elasticsearch.client.RestClient

import scala.collection.immutable
import scala.concurrent.Future

class CountryCityDal(implicit val actorSystem: ActorSystem, implicit val esClient: RestClient) {

  val indexName = "countries.and.cities"

  val writeSettings = ElasticsearchWriteSettings.Default
  val materializer = ActorMaterializer()
  implicit val ec = actorSystem.dispatcher

  def ingestCountries(countries: List[Country]): Future[immutable.Seq[WriteResult[Country, NotUsed]]] = {
    val reqs = countries.map(_.createWriteMessage())

    Source(reqs)
      .via(
        ElasticsearchFlow.create[Country](
          indexName,
          "_doc",
          writeSettings
        )
      ).runWith(Sink.seq)
  }

  def ingestCities(cities: List[City]): Future[immutable.Seq[WriteResult[City, NotUsed]]] = {

    val reqs = cities.map(_.createWriteMessage())

    Source(reqs)
      .via(
        ElasticsearchFlow.create[City](
          indexName,
          "_doc",
          writeSettings
        )
      ).runWith(Sink.seq)
  }

  def loadAllCities(): Future[List[City]] = {
    ElasticsearchSource.typed[City](
      indexName,
      None,
      searchParams = Map(
        "query" -> """ {
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
