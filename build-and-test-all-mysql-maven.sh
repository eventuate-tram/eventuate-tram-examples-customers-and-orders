#! /bin/bash

set -e

export EVENTUATE_COMMON_VERSION=0.8.0.RELEASE
export EVENTUATE_KAFKA_VERSION=0.3.0.RELEASE
export EVENTUATE_CDC_VERSION=0.5.0.RELEASE

. ./set-env-mysql.sh

docker-compose -f docker-compose-mysql-binlog-maven.yml down
docker-compose -f docker-compose-mysql-binlog-maven.yml up --build -d mysql zookeeper kafka

./wait-for-mysql.sh

docker-compose -f docker-compose-mysql-binlog-maven.yml up --build -d cdcservice

./wait-for-services.sh $DOCKER_HOST_IP "8099"

./mvnw install -DskipTests

docker-compose -f docker-compose-mysql-binlog-maven.yml up --build -d

./wait-for-services.sh $DOCKER_HOST_IP "8081 8082 8083"


cd end-to-end-tests
../mvnw test
cd ..

docker-compose -f docker-compose-mysql-binlog-maven.yml down