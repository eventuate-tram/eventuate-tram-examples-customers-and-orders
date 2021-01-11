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

migration_file="migration_scripts/${DATABASE}/migration.sql"

rm -f $migration_file
if [ "${DATABASE}" == "mysql" ]; then
  curl https://raw.githubusercontent.com/eventuate-foundation/eventuate-common/wip-db-id-gen/mysql/4.initialize-database-db-id.sql --output $migration_file --create-dirs
  cat $migration_file | ./mysql-cli.sh -i
elif [ "${DATABASE}" == "postgres" ]; then
  curl https://raw.githubusercontent.com/eventuate-foundation/eventuate-common/wip-db-id-gen/postgres/5.initialize-database-db-id.sql --output $migration_file --create-dirs
  cat $migration_file | ./postgres-cli.sh -i
elif [ "${DATABASE}" == "mssql" ]; then
  curl https://raw.githubusercontent.com/eventuate-foundation/eventuate-common/wip-db-id-gen/mssql/4.setup-db-id.sql --output $migration_file --create-dirs
  docker-compose -f docker-compose-mssql-polling.yml -f docker-compose-mssql-migration-tool.yml up --build --no-deps mssql-migration
else
  echo "Unknown Database"
  exit 99
fi
rm -f $migration_file

${dockerall}Up -P envFile=docker-compose-env-files/db-id-gen.env

./gradlew :end-to-end-tests:cleanTest :end-to-end-tests:test

./gradlew -P verifyDbIdMigration=true :migration-tests:cleanTest migration-tests:test

${dockerall}Down -P removeContainers=true