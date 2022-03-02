#! /bin/bash

docker run ${1:--it} --network=${PWD##*/}_default --rm  mongo:5.0.6 sh -c "exec /usr/bin/mongo --host mongodb customers_and_orders"
