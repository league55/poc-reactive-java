
  CURL_OPTIONS="--max-time 5 --connect-timeout 1 -s -X"

  curl ${CURL_OPTIONS} POST http://127.0.0.1:8080/api/providers -H "Content-Type: application/json" -d \
        '{
          "id": "ipProvider1",
          "host": "127.0.0.1",
          "port": 9500
        }'

  curl ${CURL_OPTIONS} POST http://127.0.0.1:8080/api/connections -H "Content-Type: application/json" -d \
        '{
          "id": "id2",
          "host": "127.0.0.1",
          "port": 8100,
          "providerAddress": "ipProvider1"
        }'

  curl ${CURL_OPTIONS} PUT http://127.0.0.1:8080/api/providers/ipProvider1/_enable

  curl ${CURL_OPTIONS} PUT http://127.0.0.1:8080/api/connections/id2/_enable
