#! /bin/bash -e

group=${1?}
topic=${2?}
offset=${3?}

docker run --network=${PWD##*/}_default --rm  eventuateio/eventuate-kafka:${EVENTUATE_MESSAGING_KAFKA_IMAGE_VERSION?} sh -c "exec kafka-consumer-groups --bootstrap-server kafka:29092 --reset-offsets --group $group --topic $topic --to-offset $offset --execute"
