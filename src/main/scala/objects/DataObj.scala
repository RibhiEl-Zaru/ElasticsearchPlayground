package objects

import akka.NotUsed
import akka.stream.alpakka.elasticsearch.WriteMessage


trait DataObj[T]{
  def createWriteMessage(): WriteMessage[T, NotUsed]
}



