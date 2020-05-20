#! /bin/bash

set -e

dockerall="./gradlew mysqlbinlogCompose"
dockerinfrastructure="./gradlew mysqlbinloginfrastructureCompose"
dockertextsearch="./gradlew mysqlbinlogwithorderhistorytextsearchserviceCompose"

${dockertextsearch}Down
${dockerall}Down
${dockerinfrastructure}Up

./gradlew assemble

${dockerall}Up

./gradlew :snapshot-tests:cleanTest :snapshot-tests:test

${dockertextsearch}Down
${dockerall}Down
