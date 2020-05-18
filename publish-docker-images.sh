#! /bin/bash -e

export DOCKER_IMAGE_TAG=latest

docker login -u ${DOCKER_USER_ID?} -p ${DOCKER_PASSWORD?}

./gradlew assemble mysqlbinlogwithjmeterComposeBuild mysqlbinlogwithjmeterComposePush

./gradlew javaDevelopmentComposePull || echo no image to pull

./gradlew javaDevelopmentComposeBuild

./gradlew javaDevelopmentComposePush