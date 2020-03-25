#! /bin/bash

docker run  $* --rm  mongo:3.0.4 sh -c "exec /usr/bin/mongo --host ${DOCKER_HOST_IP?} customers_and_orders"
