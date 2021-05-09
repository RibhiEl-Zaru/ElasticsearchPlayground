package objects

import akka.stream.alpakka.elasticsearch.WriteMessage
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import spray.json.{JsNumber, JsObject, JsString, JsValue, JsonWriter}

case class City(id: String, name: String, population: Int, country: String, my_join_field: String = "city"){
  def createWriteMessage() = {
    WriteMessage.createUpsertMessage(id = this.id, source = this)
      .withCustomMetadata(Map("routing" -> this.country))
  }
}

object City {

  implicit val decoder: Decoder[City] = deriveDecoder[City]
  implicit val encoder: Encoder[City] = deriveEncoder[City]

  implicit val jsonWriter = new JsonWriter[City] {

    def write(city: City): JsValue = {
      // TODO leverage circe.
      JsObject(
        "id" -> JsString(city.id),
        "name" -> JsString(city.name),
        "population" -> JsNumber(city.population),
        "my_join_field" -> JsObject(
          "name" -> JsString("city"),
          "parent" -> JsString(city.country)

        )
      )
    }
  }
}

