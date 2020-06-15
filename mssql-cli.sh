#! /bin/bash -e

docker run \
   --name mssqlterm --rm \
   --network=${PWD##*/}_default \
   -e MSSQL_HOST=mssql \
   -e QUERY="$1" \
   mcr.microsoft.com/mssql/server:2017-latest  \
   sh -c 'exec /opt/mssql-tools/bin/sqlcmd -S "$MSSQL_HOST" -U SA -P "Eventuate123!" -Q "$QUERY"'
