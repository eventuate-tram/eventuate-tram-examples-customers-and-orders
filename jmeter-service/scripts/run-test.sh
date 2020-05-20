#!/usr/bin/env bash -e

echo not resetting databases

# ./jmeter-service/scripts/reset-databases.sh

# eventuate-tram-examples-customers-and-orders_mongodb_1

./gradlew $* mysqlbinlogwithjmeterComposeUp

docker stop eventuate-tram-examples-customers-and-orders_cdcservice_1
docker rm eventuate-tram-examples-customers-and-orders_cdcservice_1

./jmeter-service/scripts/run-jmeter.sh

./gradlew $* mysqlbinloginfrastructureComposeUp

while true ; do
    date
    curl -s http://${DOCKER_HOST_IP:-localhost}:8099/actuator/prometheus | grep eventuate_cdc | (grep eventuate_cdc_processed_messages_total || echo not found)
    sleep 1
done

