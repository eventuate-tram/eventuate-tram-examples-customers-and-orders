#! /bin/bash -e

docker run --network=${PWD##*/}_default --rm  eventuateio/eventuate-kafka:wip-multi-arch-135 sh -c "exec kafka-topics --bootstrap-server kafka:29092 $*"
