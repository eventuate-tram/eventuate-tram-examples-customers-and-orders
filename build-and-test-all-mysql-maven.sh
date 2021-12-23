#! /bin/bash

set -e

java -version

echo SKIP_INSTALL_JDK_14=$SKIP_INSTALL_JDK_14

if [ -z "$SKIP_INSTALL_JDK_14" ] ; then
  source ./.circleci/install-jdk-14.sh
else
  echo skipped
fi

java -version

export EVENTUATE_COMMON_VERSION=0.8.0.RELEASE
export EVENTUATE_KAFKA_VERSION=0.3.0.RELEASE
export EVENTUATE_CDC_VERSION=0.6.1.RELEASE
export EVENTUATE_JAVA_BASE_IMAGE_VERSION=BUILD-5

./mvnw package -DskipTests

docker-compose -f docker-compose-mysql-binlog-maven.yml down
docker-compose -f docker-compose-mysql-binlog-maven.yml up --build -d mysql zookeeper kafka

./wait-for-mysql.sh

docker-compose -f docker-compose-mysql-binlog-maven.yml up --build -d cdcservice

./wait-for-services.sh ${DOCKER_HOST_IP:-localhost} "8099"

docker-compose -f docker-compose-mysql-binlog-maven.yml up --build -d

./wait-for-services.sh ${DOCKER_HOST_IP:-localhost} "8081 8082 8083"


./mvnw -am -pl order-history-service test-compile

docker-compose -f docker-compose-mysql-binlog-maven.yml down
