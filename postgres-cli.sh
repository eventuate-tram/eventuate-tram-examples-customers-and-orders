#! /bin/bash -e

docker run $* \
   --name postgresterm --network=${PWD##*/}_default \
   -e POSTGRES_HOST=postgres \
   --rm postgres:9.6.5 \
   sh -c 'export PGPASSWORD=eventuate; exec psql -h "$POSTGRES_HOST" -U eventuate'
