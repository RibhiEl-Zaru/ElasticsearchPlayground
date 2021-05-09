package esUtils

import io.circe.Json
import io.circe.syntax._

object QueryHelper {

  def term(field: String, value: String): Json = Json.obj("term" -> Json.obj(field -> value.asJson))
  def must(values: Json*): Json = Json.obj("must" -> Json.arr(values: _*))
  def bool(value: Json): Json = Json.obj("bool" -> value)
  def query(value: Json): Json = Json.obj("query" -> value)

}
