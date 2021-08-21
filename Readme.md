# Elasticsearch Playground

The point of this project is to demonstrate how to use various Scala ES projects to interact with an ES Cluster.

The ES Cluster will be stood up via Docker.

The ES projects and Docker setup will be described below


## Docker Setup

```
# Get ES running locally

docker compose up -d

# Wait for ES to run locally. Run following command to health check it.

curl localhost:9200             


# Bootstrap the indices with the follow script

bash ./bootstrap.sh


```

## Akka Streams
https://doc.akka.io/docs/alpakka/current/elasticsearch.html

## Elastic4s

https://sksamuel.github.io/elastic4s/docs/document/index.html