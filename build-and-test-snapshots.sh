#! /bin/bash

set -e

dockerall="./gradlew  mysqlbinlogtextsearchCompose"
dockerinfrastructure="./gradlew mysqlbinloginfrastructuretextsearchCompose"
dockercustomersandorders="./gradlew mysqlbinlogtextsearchcustomersandordersCompose"


${dockerall}Down
${dockerinfrastructure}Up

./gradlew assemble

${dockercustomersandorders}Up

./gradlew :snapshot-tests:cleanTest :snapshot-tests:test

${dockerall}Down
