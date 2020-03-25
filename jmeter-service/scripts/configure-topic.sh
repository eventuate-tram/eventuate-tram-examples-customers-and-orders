#!/usr/bin/env bash

docker exec -i eventuate-tram-examples-customers-and-orders_kafka_1 bash <<END
bin/kafka-topics.sh --zookeeper zookeeper:2181 --list
bin/kafka-topics.sh --zookeeper zookeeper:2181 --alter  --topic io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer --partitions 20
END
