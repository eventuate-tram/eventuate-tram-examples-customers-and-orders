#! /bin/bash -e

docker login -u ${DOCKER_USER_ID?} -p ${DOCKER_PASSWORD?}

./gradlew assemble mysqlbinlogwithjmeterComposeBuild mysqlbinlogwithjmeterComposePush
