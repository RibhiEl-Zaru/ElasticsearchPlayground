package EndpointSuites

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, get, path, pathPrefix, concat}


class AdminEndpointsSuite {

  val endpoints =
    concat(
      path("hello") {
        get {
          complete(HttpEntity(
            ContentTypes.`text/plain(UTF-8)`,
            "Hello, "
          ))
        }
      },
      path("world") {
        get {
          complete(HttpEntity(
            ContentTypes.`text/plain(UTF-8)`,
            "World! "
          ))
        }

      }
    )

}
