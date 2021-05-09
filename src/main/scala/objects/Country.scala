package objects

import com.fasterxml.jackson.annotation.JsonValue
import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._
import io.circe.syntax._
import spray.json
import spray.json.{JsNumber, JsObject, JsString, JsValue, JsonWriter}


case class Country(id: String, name: String, population: Int, my_join_field: String = "country")

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

