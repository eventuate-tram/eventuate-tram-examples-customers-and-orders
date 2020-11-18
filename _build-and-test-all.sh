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

./wait-for-services.sh localhost readers/${READER}/finished "8099"

migration_file="migration_scripts/add-database-id-support-to-eventuate-${DATABASE}.migration.sql"

if [ "${DATABASE}" == "mysql" ]; then
  cat $migration_file | ./mysql-cli.sh -i
elif [ "${DATABASE}" == "postgres" ]; then
  cat $migration_file | ./postgres-cli.sh -i
elif [ "${DATABASE}" == "mssql" ]; then
  ./gradlew mssqlpollingmigrationComposeUp
  sleep 10 # looks like there is no any healthcheck to wait that scrips are executed.
  ./gradlew mssqlpollingmigrationComposeDown # also, for some reason removeOrphans=true does not work and it is necessary to do explicit removal
else
  echo "Unknown Database"
  exit 99
fi


${dockerall}Up -P envFile=docker-compose-env-files/db-id-gen.env

./gradlew :end-to-end-tests:cleanTest :end-to-end-tests:test

${dockerall}Down -P removeContainers=true