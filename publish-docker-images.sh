#! /bin/bash -e

BRANCH=$(git rev-parse --abbrev-ref HEAD)

export DOCKER_IMAGE_TAG="${BRANCH}-$(date +"%Y%m%d%H%M")"

docker login -u ${DOCKER_USER_ID?} -p ${DOCKER_PASSWORD?}

./gradlew assemble mysqlbinlogComposeBuild mysqlbinlogwithjmeterComposeBuild mysqlbinlogwithjmeterComposePush

./gradlew javaDevelopmentImageComposePull || echo no image to pull

./gradlew javaDevelopmentImageComposeBuild javaDevelopmentImageComposePush
