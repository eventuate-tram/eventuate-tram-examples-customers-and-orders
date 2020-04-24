#! /bin/bash

set -e

dockerall="./gradlew mysqlbinlogCompose"
dockercdc="./gradlew mysqlbinlogcdcCompose"
dockertextsearch="./gradlew mysqlbinlogwithorderhistorytextsearchserviceCompose"

${dockertextsearch}Down
${dockerall}Down
${dockercdc}Up

./gradlew assemble

${dockerall}Up

./gradlew :snapshot-tests:cleanTest :snapshot-tests:test

${dockertextsearch}Down
${dockerall}Down
