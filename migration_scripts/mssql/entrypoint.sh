#!/bin/bash

/opt/mssql-tools/bin/sqlcmd -S $TRAM_DB_SERVER -U sa -P $TRAM_SA_PASSWORD -b -d $TRAM_DB -I -i add-database-id-support-to-eventuate-mssql.migration.sql || exit 1