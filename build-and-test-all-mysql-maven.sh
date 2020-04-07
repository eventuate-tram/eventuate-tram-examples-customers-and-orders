#! /bin/bash

set -e

export EVENTUATE_COMMON_VERSION=0.9.0.RC3
export EVENTUATE_KAFKA_VERSION=0.9.0.RC2
export EVENTUATE_CDC_VERSION=0.6.0.RC3

./mvnw package -DskipTests

docker-compose -f docker-compose-mysql-binlog-maven.yml down
docker-compose -f docker-compose-mysql-binlog-maven.yml up --build -d mysql zookeeper kafka

./wait-for-mysql.sh

docker-compose -f docker-compose-mysql-binlog-maven.yml up --build -d cdcservice

./wait-for-services.sh localhost "8099"

docker-compose -f docker-compose-mysql-binlog-maven.yml up --build -d

./wait-for-services.sh localhost "8081 8082 8083"


./mvnw -am -pl order-history-service test-compile

docker-compose -f docker-compose-mysql-binlog-maven.yml down
