#! /bin/bash -e

group=${1?}
topic=${2?}
offset=${3?}

docker run --network=${PWD##*/}_default --rm  confluentinc/cp-kafka:5.2.4 sh -c "exec kafka-consumer-groups --bootstrap-server kafka:29092 --reset-offsets --group $group --topic $topic --to-offset $offset --execute"
