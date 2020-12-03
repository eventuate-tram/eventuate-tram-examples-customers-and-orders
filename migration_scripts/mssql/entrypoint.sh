#!/bin/bash

/opt/mssql-tools/bin/sqlcmd -S $TRAM_DB_SERVER -U sa -P $TRAM_SA_PASSWORD -b -d $TRAM_DB -I -i migration.sql || exit 1