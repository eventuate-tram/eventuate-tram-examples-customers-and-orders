#! /bin/bash

set -e

java -version

if [ -z "$SKIP_INSTALL_JDK_14" ] ; then
  source ./.circleci/install-jdk-14.sh
fi

java -version

export EVENTUATE_COMMON_VERSION=0.9.0.RELEASE
export EVENTUATE_CDC_VERSION=0.6.1.RELEASE
export EVENTUATE_JAVA_BASE_IMAGE_VERSION=BUILD-5

./mvnw package -DskipTests

docker-compose -f docker-compose-mysql-binlog-maven.yml down

docker-compose -f docker-compose-mysql-binlog-maven.yml up --build -d cdcservice mongodb

./wait-for-services.sh ${DOCKER_HOST_IP:-localhost} "8099"

./mvnw -am -pl '!end-to-end-tests' package

docker-compose -f docker-compose-mysql-binlog-maven.yml up --build -d

./wait-for-services.sh ${DOCKER_HOST_IP:-localhost} "8081 8082 8083"

./mvnw -pl end-to-end-tests test

docker-compose -f docker-compose-mysql-binlog-maven.yml down
