package objects

import akka.NotUsed
import akka.stream.alpakka.elasticsearch.WriteMessage
import com.fasterxml.jackson.annotation.JsonValue
import com.sksamuel.elastic4s.Indexable
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._
import io.circe.syntax._
import spray.json
import spray.json.{JsNumber, JsObject, JsString, JsValue, JsonWriter}


case class Country(id: String, name: String, population: Int, my_join_field: String = "country") extends DataObj[Country] {
  def createWriteMessage(): WriteMessage[Country, NotUsed] = {
    WriteMessage.createUpsertMessage(id = this.id, source = this)
  }
  def json(): String =
    s""" {
       | "_id": : "${this.id},"
       |  "name" : "${this.name}",
       |  "population" : ${this.population},
       |  "my_join_field": {
       |        "name": "country"
       |    }
       | } """.stripMargin
}

object Country{

  implicit val decoder: Decoder[Country] = deriveDecoder[Country]
  implicit val encoder: Encoder[Country] = deriveEncoder[Country]

  implicit val jsonWriter = new JsonWriter[Country] {

    def write(country: Country): JsValue = {
      // TODO leverage circe.
      JsObject(
        "id" -> JsString(country.id),
        "name" -> JsString(country.name),
        "population" -> JsNumber(country.population),
        "my_join_field" -> JsObject(
          "name" -> JsString("country")
        )
      )
    }
  }
}

