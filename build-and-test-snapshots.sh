#! /bin/bash

set -e

dockerall="./gradlew mysqlbinlogCompose"
dockercdc="./gradlew mysqlbinlogcdcCompose"
dockertextsearch="./gradlew mysqlbinlogwithorderhistorytextsearchserviceCompose"

${dockertextsearch}Down
${dockerall}Down
${dockercdc}Up

./wait-for-services.sh localhost "8099"

./gradlew assemble

${dockerall}Up

./wait-for-services.sh localhost "8081 8082 8083"

./gradlew :snapshot-tests:cleanTest :snapshot-tests:test

${dockertextsearch}Down
${dockerall}Down
