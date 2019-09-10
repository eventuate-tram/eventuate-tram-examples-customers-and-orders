#! /bin/bash

set -e

dockerall="./gradlew ${DATABASE?}${MODE?}Compose"
dockercdc="./gradlew ${DATABASE?}${MODE?}cdcCompose"

. ./set-env-${DATABASE?}.sh

${dockerall}Down
${dockercdc}Build
${dockercdc}Up

./wait-for-services.sh $DOCKER_HOST_IP "8099"

./gradlew -x :end-to-end-tests:test build

${dockerall}Build
${dockerall}Up

./wait-for-services.sh $DOCKER_HOST_IP "8081 8082 8083"

./gradlew :end-to-end-tests:cleanTest :end-to-end-tests:test

${dockerall}Down
