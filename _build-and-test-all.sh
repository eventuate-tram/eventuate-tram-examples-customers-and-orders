#! /bin/bash

set -e

dockerall="./gradlew ${DATABASE?}${MODE?}Compose"
dockerinfrastructure="./gradlew ${DATABASE?}${MODE?}infrastructureCompose"

./gradlew testClasses

${dockerall}Down -P removeContainers=true
${dockerinfrastructure}Up

./gradlew -x :end-to-end-tests:test -x :snapshot-tests:test build

#Testing db cli
if [ "${DATABASE}" == "mysql" ]; then
  echo 'show databases;' | ./mysql-cli.sh -i
elif [ "${DATABASE}" == "postgres" ]; then
  echo '\l' | ./postgres-cli.sh -i
elif [ "${DATABASE}" == "mssql" ]; then
  ./mssql-cli.sh "SELECT name FROM master.sys.databases;"
else
  echo "Unknown Database"
  exit 99
fi


${dockerall}Build
${dockerall}Up

#Testing mongo cli
echo 'show dbs' |  ./mongodb-cli.sh -i

./gradlew :end-to-end-tests:cleanTest :end-to-end-tests:test

echo Testing migration

./wait-for-services.sh localhost readers/${READER}/finished "8099"

compose="docker-compose -f docker-compose-${DATABASE}-${MODE}.yml "

export EVENTUATE_MESSAGING_KAFKA_IMAGE_VERSION=$(sed -e '/^eventuateMessagingKafkaImageVersion=/!d' -e 's/eventuateMessagingKafkaImageVersion=//' < gradle.properties)
export EVENTUATE_COMMON_VERSION=$(sed -e '/^eventuateCommonImageVersion=/!d' -e 's/eventuateCommonImageVersion=//' < gradle.properties)
export EVENTUATE_CDC_VERSION=$(sed -e '/^eventuateCdcImageVersion=/!d' -e 's/eventuateCdcImageVersion=//' < gradle.properties)

$compose stop cdc-service

curl -s https://raw.githubusercontent.com/eventuate-foundation/eventuate-common/master/migration/db-id/migration.sh &> /dev/stdout | bash
$compose start cdc-service


${dockerall}Up -P envFile=docker-compose-env-files/db-id-gen.env

./gradlew :end-to-end-tests:cleanTest :end-to-end-tests:test

./gradlew -P verifyDbIdMigration=true :migration-tests:cleanTest migration-tests:test

${dockerall}Down -P removeContainers=true
