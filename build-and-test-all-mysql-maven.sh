#! /bin/bash -e

export EVENTUATE_COMMON_VERSION=$(grep eventuateCommonImageVersion= gradle.properties  | sed -e 's/.*=//')
export EVENTUATE_CDC_VERSION=$(grep eventuateCdcImageVersion= gradle.properties  | sed -e 's/.*=//')
export EVENTUATE_JAVA_BASE_IMAGE_VERSION=BUILD-5

./mvnw test-compile package -DskipTests

docker-compose -f docker-compose-mysql-binlog-maven.yml down

docker-compose -f docker-compose-mysql-binlog-maven.yml up --build -d mysql

./wait-for-mysql.sh

docker-compose -f docker-compose-mysql-binlog-maven.yml up --build -d cdc-service

./wait-for-services.sh ${DOCKER_HOST_IP:-localhost} /actuator/health 8099

./mvnw -am -pl '!end-to-end-tests' package -DskipTests

docker-compose -f docker-compose-mysql-binlog-maven.yml up --build -d

./wait-for-services.sh ${DOCKER_HOST_IP:-localhost} /actuator/health 8081 8082 8083

./mvnw -am -pl end-to-end-tests test

docker-compose -f docker-compose-mysql-binlog-maven.yml down
