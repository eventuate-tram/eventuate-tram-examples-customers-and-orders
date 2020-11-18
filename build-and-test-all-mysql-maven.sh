#! /bin/bash -e

export EVENTUATE_COMMON_VERSION=0.13.0.DB_ID_GEN.BUILD-SNAPSHOT
export EVENTUATE_CDC_VERSION=0.10.0.DB_ID_GEN.BUILD-SNAPSHOT
export EVENTUATE_JAVA_BASE_IMAGE_VERSION=BUILD-5

./mvnw test-compile package -DskipTests

docker-compose -f docker-compose-mysql-binlog-maven.yml down
docker-compose -f docker-compose-mysql-binlog-maven.yml up --build -d mysql zookeeper kafka

./wait-for-mysql.sh

docker-compose -f docker-compose-mysql-binlog-maven.yml up --build -d cdc-service

./wait-for-services.sh ${DOCKER_HOST_IP:-localhost} /actuator/health 8099

docker-compose -f docker-compose-mysql-binlog-maven.yml up --build -d

./wait-for-services.sh ${DOCKER_HOST_IP:-localhost} /actuator/health 8081 8082 8083


./mvnw -am -pl order-history-service test-compile

docker-compose -f docker-compose-mysql-binlog-maven.yml down
