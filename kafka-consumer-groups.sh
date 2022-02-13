#! /bin/bash -e

docker run --network=${PWD##*/}_default --rm  eventuateio/eventuate-kafka:${EVENTUATE_MESSAGING_KAFKA_IMAGE_VERSION?} sh -c "exec kafka-consumer-groups --bootstrap-server kafka:29092 $*"
