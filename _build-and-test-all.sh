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

if [ "${DATABASE}" == "mysql" ] || [ "${DATABASE}" = "postgres" ]; then
  ./wait-for-services.sh localhost readers/${READER}/finished "8099"

  migration_file="add-database-id-support-to-eventuate-${DATABASE}.migration.sql"

  if [ "${DATABASE}" == "mysql" ]; then
    cat $migration_file | ./mysql-cli.sh -i
  elif [ "${DATABASE}" == "postgres" ]; then
    cat $migration_file | ./postgres-cli.sh -i
  fi

  ${dockerall}Up -P envFile=docker-compose-env-files/db-id-gen.env

  ./gradlew :end-to-end-tests:cleanTest :end-to-end-tests:test
fi

${dockerall}Down -P removeContainers=true

