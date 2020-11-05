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

cat "add-database-id-support-to-eventuate-mysql.migration.sql" | ./mysql-cli.sh -i

manual_compose="docker-compose -f docker-compose-mysql-binlog.yml"
services_to_restart="order-service customer-service order-history-service"

${manual_compose} stop ${services_to_restart}
${manual_compose} rm --force ${services_to_restart}

${dockerall}Up -P envFile=dbid.env

./gradlew :end-to-end-tests:cleanTest :end-to-end-tests:test

${dockerall}Down -P removeContainers=true
