curl --location --request DELETE 'http://localhost:9200/*'

printf "\nDeleted all indices. Now generating indices\n"

curl --location --request PUT 'http://localhost:9200/countries.and.cities' \
--header 'Content-Type: application/json' \
--data-raw '{
    "settings": {
        "number_of_shards": 1
    },
    "mappings": {
        "properties": {
            "id": {
                "type": "keyword"
            },
            "name": {
                "type": "keyword"
            },
            "population" : {
                "type" : "integer"
            },
            "my_join_field": {
                "type": "join",
                "relations": {
                    "country": "city"
                }
            }
        }
    }
}'