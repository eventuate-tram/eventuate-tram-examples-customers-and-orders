#! /bin/bash

docker run ${1:--it} --network=${PWD##*/}_default --rm  mongo:3.0.4 sh -c "exec /usr/bin/mongo --host mongodb customers_and_orders"
