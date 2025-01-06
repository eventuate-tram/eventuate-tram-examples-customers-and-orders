#! /bin/bash

docker run ${1:--it} --network=CustomersAndOrdersEndToEndTest --rm  mongo:8.0.4 sh -c "exec /usr/bin/mongosh --host order-history-service-db customers_and_orders"
