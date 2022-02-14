#! /bin/bash -e

. ./_set-image-version-env-vars.sh

export JAR_DIR=target

./mvnw test-compile package -DskipTests

docker-compose -f docker-compose-mysql-binlog.yml down

docker-compose -f docker-compose-mysql-binlog.yml up --build -d mysql

./wait-for-mysql.sh

docker-compose -f docker-compose-mysql-binlog.yml up --build -d cdc-service

./wait-for-services.sh ${DOCKER_HOST_IP:-localhost} /actuator/health 8099

./mvnw -am -pl '!end-to-end-tests' package -DskipTests

docker-compose -f docker-compose-mysql-binlog.yml up --build -d

./wait-for-services.sh ${DOCKER_HOST_IP:-localhost} /actuator/health 8081 8082 8083

./mvnw -am -pl end-to-end-tests test

docker-compose -f docker-compose-mysql-binlog.yml down -t 1
