package alpakka

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import akka.stream.alpakka.elasticsearch.{ElasticsearchSourceSettings, ElasticsearchWriteSettings, WriteMessage}
import akka.stream.alpakka.elasticsearch.scaladsl.{ElasticsearchFlow, ElasticsearchSource}
import akka.stream.scaladsl.{Sink, Source}
import objects.{City, Country}
import org.elasticsearch.client.RestClient

class CountryCityDal(implicit val actorSystem: ActorSystem, implicit val esClient: RestClient) {

  val indexName = "countries.and.cities"

  val writeSettings = ElasticsearchWriteSettings.Default
  val materializer = ActorMaterializer()
  implicit val ec = actorSystem.dispatcher

  def ingestCountries(countries: List[Country]) = {
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

  def ingestCities(cities: List[City]) = {

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

  def loadAllCities() = {
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
  }



}
