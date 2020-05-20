#! /bin/bash

set -e

dockerall="./gradlew  mysqlbinlogtextsearchCompose"
dockerinfrastructure="./gradlew mysqlbinloginfrastructuretextsearchCompose"

${dockerall}Down
#${dockerinfrastructure}Up

#./gradlew assemble
#
#${dockerall}Up
#
#./gradlew :snapshot-tests:cleanTest :snapshot-tests:test
#
#${dockerall}Down
